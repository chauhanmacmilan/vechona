package com.vechona.com.data.remote.apiResponse;

import com.google.gson.annotations.SerializedName;

public class UserOrderListResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private UserOrderListData userOrderListData;

    public String getStatus() {
        return status;
    }

    public UserOrderListData getUserOrderListData() {
        return userOrderListData;
    }
}
