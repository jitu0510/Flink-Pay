package com.rmgYantra.loginapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountDetails {
    private String accountNumber;
    private String accountName;
    private String bankName;
    private String bankBranchName;
    private int amount;
}
