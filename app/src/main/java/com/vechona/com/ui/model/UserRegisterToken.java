package com.vechona.com.ui.model;

import com.google.gson.annotations.SerializedName;

public class UserRegisterToken {

    @SerializedName("USER_ID")
    private int userId;

    @SerializedName("TOKEN")
    private String token;

    public UserRegisterToken(int userId, String token) {
        this.userId = userId;
        this.token = token;
    }
}