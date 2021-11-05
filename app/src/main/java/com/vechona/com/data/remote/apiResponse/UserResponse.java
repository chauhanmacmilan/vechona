package com.vechona.com.data.remote.apiResponse;

import com.google.gson.annotations.SerializedName;

public class UserResponse {

    @SerializedName("status")
    private String Status;

    @SerializedName("data")
    private UserResponse.Data data;

    public String getStatus() {
        return Status;
    }

    public UserResponse.Data getData() {
        return data;
    }

    public class Data {
        @SerializedName("result")
        private String result;

        public String getResult() {
            return result;
        }
    }
}