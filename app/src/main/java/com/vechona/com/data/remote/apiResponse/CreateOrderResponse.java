package com.vechona.com.data.remote.apiResponse;

import com.google.gson.annotations.SerializedName;

public class CreateOrderResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private CreateOrderResponseData orderResponseData;

    public String getStatus() {
        return status;
    }

    public CreateOrderResponseData getOrderResponseData() {
        return orderResponseData;
    }
}
