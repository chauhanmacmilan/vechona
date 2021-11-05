package com.vechona.com.data.remote.apiResponse;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private AuthResponseData authResponseData;

    public String getStatus() {
        return status;
    }

    public AuthResponseData getAuthResponseData() {
        return authResponseData;
    }
}
