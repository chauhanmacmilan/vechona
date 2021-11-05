package com.vechona.com.data.remote.apiResponse;

import com.google.gson.annotations.SerializedName;

public class GetCartResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private CartResponseData cartResponseData;

    public String getStatus() {
        return status;
    }

    public CartResponseData getCartResponseData() {
        return cartResponseData;
    }
}
