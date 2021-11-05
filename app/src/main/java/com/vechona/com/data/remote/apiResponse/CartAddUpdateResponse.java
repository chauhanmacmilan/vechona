package com.vechona.com.data.remote.apiResponse;

import com.google.gson.annotations.SerializedName;

public class CartAddUpdateResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private Data data;

    public String getStatus() {
        return status;
    }

    public Data getData() {
        return data;
    }

    private class Data {
        @SerializedName("result")
        private String errorMessage;
    }
}
