package com.vechona.com.data.remote.apiResponse;

import com.google.gson.annotations.SerializedName;

public class OrderDetailsResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private OrderDetailsResponseData data;

    public String getStatus() {
        return status;
    }

    public OrderDetailsResponseData getData() {
        return data;
    }

}
