package com.vechona.com.ui.model;

import com.google.gson.annotations.SerializedName;

public class BankAccountDetails {

    @SerializedName("ACCOUNT_NUMBER")
    private String accountNo;

    @SerializedName("ACCOUNT_NAME")
    private String accountHolderName;

    @SerializedName("IFSC_CODE")
    private String ifscCode;

    @SerializedName("USER_ID")
    private int userId;

    public BankAccountDetails(String accountNo, String accountHolderName, String ifscCode, int userId) {
        this.accountNo = accountNo;
        this.accountHolderName = accountHolderName;
        this.ifscCode = ifscCode;
        this.userId = userId;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public int getUserId() {
        return userId;
    }

}
