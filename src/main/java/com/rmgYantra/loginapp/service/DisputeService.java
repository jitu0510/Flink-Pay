package com.rmgYantra.loginapp.service;

import com.rmgYantra.loginapp.model.*;
import com.rmgYantra.loginapp.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class DisputeService {

    @Autowired
    private DisputeRepositoryForBank1 disputeRepositoryForBank1;

    @Autowired
    private DisputeRepositoryForBank2 disputeRepositoryForBank2;

    @Autowired
    private AmountTransferRepoForBank1 amountTransferRepoForBank1;

    @Autowired
    private AmountTransferRepoForBank2 amountTransferRepoForBank2;

    public ResponseEntity<?> raiseDefect(String referenceNumber, String disputeDescription, String username){

        if(username.equals("rmgyantra")){
            if(disputeRepositoryForBank1.existsById(referenceNumber)){
                return new ResponseEntity<>("Dispute is already raised for this referenceNumber="+referenceNumber,HttpStatus.BAD_REQUEST);
            }
            MoneyTransferForBank1 moneyTransferForBank1 = amountTransferRepoForBank1.findById(referenceNumber).get();
            DisputeForBank1 dispute = new DisputeForBank1();
            dispute.setReferenceNumber(referenceNumber);
            dispute.setBankName(moneyTransferForBank1.getBankName());
            dispute.setBankBranch(moneyTransferForBank1.getBankBranch());
            dispute.setDisputeDescription(disputeDescription);
            dispute.setCurrency(moneyTransferForBank1.getCurrency());
            dispute.setBeneficiaryAccountName(moneyTransferForBank1.getBeneficiaryAccountName());
            dispute.setBeneficiaryCreditAccountNumber(moneyTransferForBank1.getBeneficiaryCreditAccountNumber());
            dispute.setChargesCode(moneyTransferForBank1.getChargesCode());
            dispute.setBeneficiaryType(moneyTransferForBank1.getBeneficiaryType());
            dispute.setPassed(moneyTransferForBank1.isPassed());
            dispute.setRemarks(moneyTransferForBank1.getRemarks());
            dispute.setStatus("DISPUTE_RAISED");
            dispute.setBeneficiaryMobileNumber(moneyTransferForBank1.getBeneficiaryMobileNumber());
            dispute.setTransferAmount(moneyTransferForBank1.getTransferAmount());
            dispute.setSenderAccountName(moneyTransferForBank1.getSenderAccountName());
            dispute.setSenderAccountNumber(moneyTransferForBank1.getSenderAccountNumber());
            dispute.setTransferDate(moneyTransferForBank1.getTransferDate());
            dispute.setDisputeRaiseTimeStamp(getTimeStamp());
            DisputeForBank1 dispute1 = disputeRepositoryForBank1.save(dispute);
            return new ResponseEntity<>(dispute1, HttpStatus.CREATED);
        }else if(username.equals("hdfcmaker")){
            if(disputeRepositoryForBank2.existsById(referenceNumber)){
                return new ResponseEntity<>("Dispute is already raised for this referenceNumber="+referenceNumber,HttpStatus.BAD_REQUEST);
            }
            MoneyTransferForBank2 moneyTransferForBank2 = amountTransferRepoForBank2.findById(referenceNumber).get();
            DisputeForBank2 dispute = new DisputeForBank2();
            dispute.setReferenceNumber(referenceNumber);
            dispute.setBankName(moneyTransferForBank2.getBankName());
            dispute.setBankBranch(moneyTransferForBank2.getBankBranch());
            dispute.setDisputeDescription(disputeDescription);
            dispute.setCurrency(moneyTransferForBank2.getCurrency());
            dispute.setBeneficiaryAccountName(moneyTransferForBank2.getBeneficiaryAccountName());
            dispute.setBeneficiaryCreditAccountNumber(moneyTransferForBank2.getBeneficiaryCreditAccountNumber());
            dispute.setChargesCode(moneyTransferForBank2.getChargesCode());
            dispute.setBeneficiaryType(moneyTransferForBank2.getBeneficiaryType());
            dispute.setPassed(moneyTransferForBank2.isPassed());
            dispute.setRemarks(moneyTransferForBank2.getRemarks());
            dispute.setStatus("DISPUTE_RAISED");
            dispute.setBeneficiaryMobileNumber(moneyTransferForBank2.getBeneficiaryMobileNumber());
            dispute.setTransferAmount(moneyTransferForBank2.getTransferAmount());
            dispute.setSenderAccountName(moneyTransferForBank2.getSenderAccountName());
            dispute.setSenderAccountNumber(moneyTransferForBank2.getSenderAccountNumber());
            dispute.setTransferDate(moneyTransferForBank2.getTransferDate());
            dispute.setDisputeRaiseTimeStamp(getTimeStamp());
            DisputeForBank2 disputeForBank2 = disputeRepositoryForBank2.save(dispute);
            return new ResponseEntity<>(disputeForBank2, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(null,HttpStatus.UNAUTHORIZED);
        }
    }

    private String getTimeStamp(){
        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();
        // Define the desired format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        // Format the current date and time
        String formattedTimestamp = now.format(formatter);
        // Get the current date
        LocalDate today = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
       // String formattedDate = today.format(dateFormatter);
        return formattedTimestamp;
    }
}
