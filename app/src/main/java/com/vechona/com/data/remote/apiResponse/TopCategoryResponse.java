package com.vechona.com.data.remote.apiResponse;

import com.google.gson.annotations.SerializedName;

public class TopCategoryResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private TopCategoryResponseData topCategoryResponseData;

    public String getStatus() {
        return status;
    }

    public TopCategoryResponseData getTopCategoryResponseData() {
        return topCategoryResponseData;
    }
}
