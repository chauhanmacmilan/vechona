package com.vechona.com.data.remote.apiResponse;

import com.google.gson.annotations.SerializedName;
import com.vechona.com.ui.model.BankAccountDetails;
import com.vechona.com.ui.model.User;

public class AuthResponseData {

    @SerializedName("user")
    private User user;

    @SerializedName("accessToken")
    private String accessToken;

    @SerializedName("accountDetails")
    private BankAccountDetails accountDetails;

    public User getUser() {
        return user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public BankAccountDetails getAccountDetails() {
        return accountDetails;
    }
}
