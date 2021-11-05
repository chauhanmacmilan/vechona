package com.vechona.com.data.remote.apiResponse;

import com.google.gson.annotations.SerializedName;

public class UserUpdateResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("error")
    private Error error;

    public String getStatus() {
        return status;
    }

    public Error getError() {
        return error;
    }

    private class Error {
        @SerializedName("errorMessage")
        private String errorMessage;
    }
}