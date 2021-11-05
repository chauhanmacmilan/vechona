package com.vechona.com.data.remote.apiResponse;

import com.google.gson.annotations.SerializedName;

public class CollectionItemResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private CollectionItemResponseData collectionItemResponseData;

    public String getStatus() {
        return status;
    }

    public CollectionItemResponseData getCollectionItemResponseData() {
        return collectionItemResponseData;
    }
}
