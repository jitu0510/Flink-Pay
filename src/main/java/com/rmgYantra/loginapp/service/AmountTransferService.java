package com.rmgYantra.loginapp.service;

import com.rmgYantra.loginapp.model.*;
import com.rmgYantra.loginapp.repo.AmountTransferRepoForBank1;
import com.rmgYantra.loginapp.repo.AmountTransferRepoForBank2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class AmountTransferService {

    @Autowired
    private AmountTransferRepoForBank1 amountTransferRepoForBank1;

    @Autowired
    private AmountTransferRepoForBank2 amountTransferRepoForBank2;

    @Autowired
    private ReferenceNumberGenerator referenceNumberGenerator;

    @Autowired
    private InboundTransactionService inboundTransactionService;

    @Autowired
    private TransactionService transactionService;

    @Transactional
    public ResponseEntity<?> transferAmount(MoneyTransfer moneyTransferTemp, String username) throws Exception {

        if(username.equals("rmgyantra")){
            if( !moneyTransferTemp.getSenderAccountNumber().startsWith("1051")){
                return new ResponseEntity<>("Invalid Sender Account Number", HttpStatus.BAD_REQUEST);
            }
        }else if(username.equals("hdfcmaker")){
            if(!moneyTransferTemp.getSenderAccountNumber().startsWith("2101")){
                return new ResponseEntity<>("Invalid Sender Account Number", HttpStatus.BAD_REQUEST);
            }
        }
        MoneyTransferForBank1 moneyTransferForBank1 = null;
        MoneyTransferForBank2 moneyTransferForBank2 = null;
        // Generate a unique reference number
        String referenceNumber = referenceNumberGenerator.generateReferenceNumber();



        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();
        // Define the desired format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        // Format the current date and time
        String formattedTimestamp = now.format(formatter);
        // Get the current date
        LocalDate today = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = today.format(dateFormatter);

        moneyTransferTemp.setReferenceNumber(referenceNumber);
        Map<String, String> replacements = new HashMap<>();
        replacements.put("Abraham Obando", moneyTransferTemp.getSenderAccountName());
        replacements.put("Origami", moneyTransferTemp.getBeneficiaryAccountName());
        replacements.put("01301000P00810083", referenceNumber);
        replacements.put("1999.99", moneyTransferTemp.getTransferAmount());
        replacements.put("13011300003", moneyTransferTemp.getSenderAccountNumber());
        replacements.put("6176779819911", moneyTransferTemp.getBeneficiaryCreditAccountNumber());
        replacements.put("2024-12-04T04:50:34", formattedTimestamp);
        replacements.put("2024-12-04", formattedDate);

        // Load the file as a resource stream
        ClassPathResource resource = new ClassPathResource("swift.txt");
        String fileContent;
        try (InputStream inputStream = resource.getInputStream()) {
            fileContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }

        // Apply replacements
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            fileContent = fileContent.replace(entry.getKey(), entry.getValue());
        }

        // Create a temporary file to hold the modified content
        File tempFile = File.createTempFile("modified_swift_", ".txt");
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(fileContent.getBytes(StandardCharsets.UTF_8));
        }

        MultipartFile multipartFile = new FileMultipartFile(tempFile);


        moneyTransferTemp.setFileData(fileContent.getBytes(StandardCharsets.UTF_8));
        moneyTransferTemp.setTransferDate(formattedDate);

        // Call external API
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://49.249.29.5:8098/verify-details";

        HttpHeaders headers = new HttpHeaders();

        headers.set("Content-Type", "application/json");

        AccountDetails accountDetails = new AccountDetails();
        accountDetails.setAmount(5000);
        accountDetails.setAccountName(moneyTransferTemp.getBeneficiaryAccountName());
        accountDetails.setAccountNumber(moneyTransferTemp.getBeneficiaryCreditAccountNumber());
        accountDetails.setBankName(moneyTransferTemp.getBankName());
        accountDetails.setBankBranchName(moneyTransferTemp.getBankBranch());
        HttpEntity<AccountDetails> requestEntity = new HttpEntity<>(accountDetails, headers);
        try {
            ResponseEntity<String> apiResponse = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);

            // Check response status
            if (apiResponse.getStatusCode().is2xxSuccessful()) {
                log.info("API call successful: {}", apiResponse.getBody());
                moneyTransferTemp.setPassed(true);
                // Process the file and save the transaction
                transactionService.fetchTransactionDetailsFromFileAndSaveInDb(multipartFile);
            }
        }catch (Exception e){
            log.error("Invalid data= {}",requestEntity);
        }
        if(moneyTransferTemp.getSenderAccountNumber().startsWith("1051")){
            moneyTransferForBank1 = new MoneyTransferForBank1();
            setMoneyTransferDetailsForBank1(moneyTransferForBank1, moneyTransferTemp,referenceNumber);
            MoneyTransferForBank1 savedTransfer = amountTransferRepoForBank1.save(moneyTransferForBank1);
            //System.out.println("Modified file saved. Path: " + tempFile.getAbsolutePath() + ", Name: " + tempFile.getName());

            return new ResponseEntity<>(savedTransfer, HttpStatus.CREATED);
        }else{
            moneyTransferForBank2 = new MoneyTransferForBank2();
            setMoneyTransferDetailsForBank2(moneyTransferForBank2,moneyTransferTemp,referenceNumber);
            MoneyTransferForBank2 savedTransfer = amountTransferRepoForBank2.save(moneyTransferForBank2);
            //System.out.println("Modified file saved. Path: " + tempFile.getAbsolutePath() + ", Name: " + tempFile.getName());
            return new ResponseEntity<>(savedTransfer, HttpStatus.CREATED);
        }
    }

    public void setMoneyTransferDetailsForBank1(MoneyTransferForBank1 moneyTransferForBank1, MoneyTransfer moneyTransfer,String referenceNumber){
        moneyTransferForBank1.setReferenceNumber(referenceNumber);
        moneyTransferForBank1.setBankBranch(moneyTransfer.getBankBranch());
        moneyTransferForBank1.setTransferAmount(moneyTransfer.getTransferAmount());
        moneyTransferForBank1.setBankName(moneyTransfer.getBankName());
        moneyTransferForBank1.setTransferDate(moneyTransfer.getTransferDate());
        moneyTransferForBank1.setPassed(moneyTransfer.isPassed());
        moneyTransferForBank1.setCurrency(moneyTransfer.getCurrency());
        moneyTransferForBank1.setBeneficiaryAccountName(moneyTransfer.getBeneficiaryAccountName());
        moneyTransferForBank1.setBeneficiaryCreditAccountNumber(moneyTransfer.getBeneficiaryCreditAccountNumber());
        moneyTransferForBank1.setRemarks(moneyTransfer.getRemarks());
        moneyTransferForBank1.setBeneficiaryMobileNumber(moneyTransfer.getBeneficiaryMobileNumber());
        moneyTransferForBank1.setSenderAccountName(moneyTransfer.getSenderAccountName());
        moneyTransferForBank1.setSenderAccountNumber(moneyTransfer.getSenderAccountNumber());
        moneyTransferForBank1.setChargesCode(moneyTransfer.getChargesCode());
        moneyTransferForBank1.setBeneficiaryType(moneyTransfer.getBeneficiaryType());
        moneyTransferForBank1.setFileData(moneyTransfer.getFileData());
    }
    public void setMoneyTransferDetailsForBank2(MoneyTransferForBank2 moneyTransferForBank2, MoneyTransfer moneyTransfer,String referenceNumber){
        moneyTransferForBank2.setReferenceNumber(referenceNumber);
        moneyTransferForBank2.setBankBranch(moneyTransfer.getBankBranch());
        moneyTransferForBank2.setTransferAmount(moneyTransfer.getTransferAmount());
        moneyTransferForBank2.setBankName(moneyTransfer.getBankName());
        moneyTransferForBank2.setTransferDate(moneyTransfer.getTransferDate());
        moneyTransferForBank2.setPassed(moneyTransfer.isPassed());
        moneyTransferForBank2.setCurrency(moneyTransfer.getCurrency());
        moneyTransferForBank2.setBeneficiaryAccountName(moneyTransfer.getBeneficiaryAccountName());
        moneyTransferForBank2.setBeneficiaryCreditAccountNumber(moneyTransfer.getBeneficiaryCreditAccountNumber());
        moneyTransferForBank2.setRemarks(moneyTransfer.getRemarks());
        moneyTransferForBank2.setBeneficiaryMobileNumber(moneyTransfer.getBeneficiaryMobileNumber());
        moneyTransferForBank2.setSenderAccountName(moneyTransfer.getSenderAccountName());
        moneyTransferForBank2.setSenderAccountNumber(moneyTransfer.getSenderAccountNumber());
        moneyTransferForBank2.setChargesCode(moneyTransfer.getChargesCode());
        moneyTransferForBank2.setBeneficiaryType(moneyTransfer.getBeneficiaryType());
        moneyTransferForBank2.setFileData(moneyTransfer.getFileData());
    }

}
