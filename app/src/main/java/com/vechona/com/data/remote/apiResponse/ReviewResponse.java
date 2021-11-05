package com.vechona.com.data.remote.apiResponse;

import com.google.gson.annotations.SerializedName;

public class ReviewResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private ReviewResponseData reviewResponseData;

    public String getStatus() {
        return status;
    }

    public ReviewResponseData getReviewResponseData() {
        return reviewResponseData;
    }
}
