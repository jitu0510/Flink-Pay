package com.rmgYantra.loginapp.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "inbound_transactions_bank1")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InboundTransactionForBank1 {
    @Id
    private String referenceNumber;
    private String debtorAccountName;
    private String debtorAccountNumber;
    private String debtorAgentBICCode;

    private String timeStamp;
    private String currency;
    private String amount;
    private String settlementDate;

    private String creditorAccountName;
    private String creditorAccountNumber;
    private String creditorAgentBICCode;

    private String remittanceInformation;
}
