package com.rmgYantra.loginapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoneyTransfer {

    private String referenceNumber;
    private String currency;
    private String transferDate;
    private String senderAccountNumber;
    private String senderAccountName;
    private String chargesCode;
    private String remarks;
    private String beneficiaryType;
    private String beneficiaryCreditAccountNumber;
    private String beneficiaryAccountName;
    private String transferAmount;
    private String beneficiaryMobileNumber;
    private String bankBranch;
    private String bankName;

    private byte[] fileData; // Store the file as a byte array

    private boolean passed;
}