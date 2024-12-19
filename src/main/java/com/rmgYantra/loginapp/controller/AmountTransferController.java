package com.rmgYantra.loginapp.controller;

import com.rmgYantra.loginapp.model.MoneyTransfer;
import com.rmgYantra.loginapp.model.MoneyTransferForBank1;
import com.rmgYantra.loginapp.model.MoneyTransferForBank2;
import com.rmgYantra.loginapp.model.Transaction;
import com.rmgYantra.loginapp.repo.AmountTransferRepoForBank1;
import com.rmgYantra.loginapp.repo.AmountTransferRepoForBank2;
import com.rmgYantra.loginapp.service.AmountTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/amount-transfer")
public class AmountTransferController {

    @Autowired
    private AmountTransferService amountTransferService;

    @Autowired
    private AmountTransferRepoForBank1 amountTransferRepoForBank1;

    @Autowired
    private AmountTransferRepoForBank2 amountTransferRepoForBank2;

    @PostMapping("/{username}")
    public ResponseEntity<?> transferAmount(@RequestBody MoneyTransfer moneyTransfer,@PathVariable String username) throws Exception {
        return amountTransferService.transferAmount(moneyTransfer,username);
    }
    @GetMapping("/count/{username}")
    public long countTransfers(@PathVariable String username) {
        if(username.equals("rmgyantra")){
            return amountTransferRepoForBank1.count();
        }
        else if(username.equals("hdfcmaker")){
           return amountTransferRepoForBank2.count();
        }
        return 0;
    }

    @GetMapping("/all/rmgyantra")
    public Page<MoneyTransferForBank1> getAllTransfersForBank1(Pageable pageable) {
        return amountTransferRepoForBank1.findAll(pageable);
    }
    @GetMapping("/all/hdfcmaker")
    public Page<MoneyTransferForBank2> getAllTransfersForBank2(Pageable pageable) {
        return amountTransferRepoForBank2.findAll(pageable);
    }
  /*  @GetMapping("/all-outbounds/{username}")
    public ResponseEntity<?> getAllTransfersWithoutPagination(@PathVariable String username) {
//        Page<MoneyTransfer> transfers = amountTransferRepo.findAll(pageable);
//        List<MoneyTransfer> moneyTransferList = null;
//
//        if(username.equals("rmgyantra")){
//            moneyTransferList = transfers.getContent().stream().filter(t -> t.getSenderAccountNumber().startsWith("1051")).collect(Collectors.toList());
//        }
//        else if(username.equals("hdfcmaker")){
//            moneyTransferList = transfers.getContent().stream().filter(t -> t.getSenderAccountNumber().startsWith("2101")).collect(Collectors.toList());
//        }
//        return new PageImpl<>(moneyTransferList, pageable, moneyTransferList.size());
        List<MoneyTransfer> transfers = amountTransferRepo.findAll();
        List<MoneyTransfer> moneyTransferList = null;

        if(username.equals("rmgyantra")){
            moneyTransferList = transfers.stream().filter(t -> t.getSenderAccountNumber().startsWith("1051")).collect(Collectors.toList());
        }
        else if(username.equals("hdfcmaker")){
            moneyTransferList = transfers.stream().filter(t -> t.getSenderAccountNumber().startsWith("2101")).collect(Collectors.toList());
        }
        System.out.println(moneyTransferList.size());
        return new ResponseEntity<>(moneyTransferList,HttpStatus.OK);
    }*/


    @GetMapping("/download/{username}")
    public ResponseEntity<?> downloadFile(@RequestParam String referenceNumber,@PathVariable String username) {
        if(username.equals("rmgyantra")) {
            // Fetch the MoneyTransfer record from the database
            MoneyTransferForBank1 moneyTransfer = amountTransferRepoForBank1.findByReferenceNumber(referenceNumber);

            if (moneyTransfer == null || moneyTransfer.getFileData() == null) {
                return new ResponseEntity<>("File not found for reference number: " + referenceNumber, HttpStatus.NOT_FOUND);
            }
            // Create HTTP headers for file download
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + referenceNumber + ".txt");
            headers.add(HttpHeaders.CONTENT_TYPE, "text/plain");

            // Return the file data as a ResponseEntity
            return new ResponseEntity<>(moneyTransfer.getFileData(), headers, HttpStatus.OK);
        }else if(username.equals("hdfcmaker")){
            // Fetch the MoneyTransfer record from the database
            MoneyTransferForBank2 moneyTransfer = amountTransferRepoForBank2.findByReferenceNumber(referenceNumber);

            if (moneyTransfer == null || moneyTransfer.getFileData() == null) {
                return new ResponseEntity<>("File not found for reference number: " + referenceNumber, HttpStatus.NOT_FOUND);
            }
            // Create HTTP headers for file download
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + referenceNumber + ".txt");
            headers.add(HttpHeaders.CONTENT_TYPE, "text/plain");

            // Return the file data as a ResponseEntity
            return new ResponseEntity<>(moneyTransfer.getFileData(), headers, HttpStatus.OK);

        }
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }

}
