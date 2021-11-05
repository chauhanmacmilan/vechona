package com.vechona.com.data.remote.apiResponse;

import com.google.gson.annotations.SerializedName;
import com.vechona.com.ui.model.Review;

import java.util.List;

public class ReviewResponseData {

    @SerializedName("reviewList")
    private List<Review> reviews;

    @SerializedName("result")
    private String result;

    public List<Review> getReviews() {
        return reviews;
    }

    public String getResult() {

        return result;
    }
}
