package com.rmgYantra.loginapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "disputes_bank1")
public class DisputeForBank1 {

    @Id
    private String referenceNumber;
    private String disputeRaiseTimeStamp;
    private String disputeDescription;
    private String status;

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
    private boolean passed;

}
