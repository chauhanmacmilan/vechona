package com.vechona.com.data.remote.apiResponse;

import com.google.gson.annotations.SerializedName;
import com.vechona.com.ui.model.Catalog;

import java.util.List;

public class CollectionItemResponseData {

    @SerializedName("catalogList")
    private List<Catalog> cataloglist;

    public List<Catalog> getCataloglist() {
        return cataloglist;
    }
}
