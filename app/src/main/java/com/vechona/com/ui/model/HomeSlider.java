package com.vechona.com.ui.model;

import com.google.gson.annotations.SerializedName;

public class HomeSlider {

    @SerializedName("SLIDER_ID")
    private int sliderID;

    @SerializedName("IMAGE_URL")
    private String sliderImageUrl;

    @SerializedName("COLLECTION_ID")
    private int sliderCollectionID;

    @SerializedName("COLLECTION_NAME")
    private String collectionName;

    public int getSliderID() {
        return sliderID;
    }

    public String getSliderImageUrl() {
        return sliderImageUrl;
    }

    public int getSliderCollectionID() {
        return sliderCollectionID;
    }

    public String getCollectionName() {
        return collectionName;
    }
}
