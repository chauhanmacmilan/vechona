package com.vechona.com.data.remote.apiResponse;

import com.google.gson.annotations.SerializedName;

public class SettingResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private SettingResponseData settingResponseData;

    public String getStatus() {
        return status;
    }

    public SettingResponseData getSettingResponseData() {
        return settingResponseData;
    }
}
