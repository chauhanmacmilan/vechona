package com.vechona.com.data.remote.apiResponse;

import com.google.gson.annotations.SerializedName;

public class CollectionResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private CollectionResponseData collectionResponseData;

    public String getStatus() {
        return status;
    }

    public CollectionResponseData getCollectionResponseData() {
        return collectionResponseData;
    }
}
