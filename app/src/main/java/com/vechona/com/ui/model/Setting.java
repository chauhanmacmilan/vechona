package com.vechona.com.ui.model;

import com.google.gson.annotations.SerializedName;

public class Setting {

    @SerializedName("NAME")
    private String name;

    @SerializedName("VALUE")
    private String value;

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
