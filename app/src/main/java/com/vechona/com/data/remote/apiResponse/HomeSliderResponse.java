package com.vechona.com.data.remote.apiResponse;

import com.google.gson.annotations.SerializedName;

public class HomeSliderResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private HomeSliderResponseData homeSliderResponseData;

    public HomeSliderResponseData getHomeSliderResponseData() {
        return homeSliderResponseData;
    }

    public String getStatus() {
        return status;
    }
}
