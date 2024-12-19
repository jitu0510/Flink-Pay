package com.rmgYantra.loginapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "money_transfers_bank2")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoneyTransferForBank2 {
    @Id
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

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] fileData; // Store the file as a byte array

    private boolean passed;
}
