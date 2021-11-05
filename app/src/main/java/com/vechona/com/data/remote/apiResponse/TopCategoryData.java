package com.vechona.com.data.remote.apiResponse;

import com.google.gson.annotations.SerializedName;
import com.vechona.com.ui.model.Catalog;

import java.util.List;

public class TopCategoryData {

    @SerializedName("catalogList")
    private List<Catalog> categoryList;

    public List<Catalog> getCategoryList() {
        return categoryList;
    }
}