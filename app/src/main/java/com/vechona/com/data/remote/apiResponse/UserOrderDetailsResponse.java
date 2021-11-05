package com.vechona.com.data.remote.apiResponse;

import com.google.gson.annotations.SerializedName;

public class UserOrderDetailsResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private UserOrderDetailsData userOrderDetailsData;

    public String getStatus() {
        return status;
    }

    public UserOrderDetailsData getUserOrderDetailsData() {
        return userOrderDetailsData;
    }
}
