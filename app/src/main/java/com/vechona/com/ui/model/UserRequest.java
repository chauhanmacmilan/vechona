package com.vechona.com.ui.model;

import com.google.gson.annotations.SerializedName;

public class UserRequest {

    @SerializedName("MOBILE_NUMBER")
    private String phoneNumber;

    @SerializedName("FIREBASE_ID")
    private String firebaseUserId;

    public UserRequest(String phoneNumber, String uid) {
        this.phoneNumber = phoneNumber;
        this.firebaseUserId = uid;
    }
}
