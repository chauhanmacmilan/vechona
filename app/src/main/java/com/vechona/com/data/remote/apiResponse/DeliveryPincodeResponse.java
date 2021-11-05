package com.vechona.com.data.remote.apiResponse;

import com.google.gson.annotations.SerializedName;

public class DeliveryPincodeResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private boolean data;

    public String getStatus() {
        return status;
    }

    public boolean isData() {
        return data;
    }
}
