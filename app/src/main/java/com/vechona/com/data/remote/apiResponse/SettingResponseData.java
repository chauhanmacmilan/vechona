package com.vechona.com.data.remote.apiResponse;

import com.google.gson.annotations.SerializedName;
import com.vechona.com.ui.model.Setting;

import java.util.List;

public class SettingResponseData {

    @SerializedName("settingList")
    private List<Setting> settings;

    public List<Setting> getSettings() {
        return settings;
    }
}