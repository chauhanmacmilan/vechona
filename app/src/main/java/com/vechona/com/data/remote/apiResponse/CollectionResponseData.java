package com.vechona.com.data.remote.apiResponse;

import com.google.gson.annotations.SerializedName;
import com.vechona.com.ui.model.Collection;

import java.util.List;

public class CollectionResponseData {

    @SerializedName("collectionList")
    private List<Collection> collectionList;

    public List<Collection> getCollectionList() {
        return collectionList;
    }
}
