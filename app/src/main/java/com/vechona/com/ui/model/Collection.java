package com.vechona.com.ui.model;

import com.google.gson.annotations.SerializedName;


public class Collection {

    @SerializedName("COLLECTION_ID")
    private int collectionId;

    @SerializedName("COLLECTION_NAME")
    private String collectionName;

    @SerializedName("COLLECTION_DESCRIPTION")
    private String collectionDesc;

    @SerializedName("COLLECTION_IMAGE_URL")
    private String collectionImageUrl;

    public int getCollectionId() {
        return collectionId;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public String getCollectionDesc() {
        return collectionDesc;
    }

    public String getCollectionImageUrl() {
        return collectionImageUrl;
    }
}

