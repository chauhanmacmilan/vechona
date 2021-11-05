package com.vechona.com.data.remote.apiResponse;

import com.google.gson.annotations.SerializedName;

public class TopCategory {

    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private TopCategoryData topCategoryData;

    public String getStatus() {
        return status;
    }

    public TopCategoryData getTopCategoryData() {
        return topCategoryData;
    }
}