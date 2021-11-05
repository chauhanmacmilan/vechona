package com.vechona.com.data.remote.apiResponse;

import com.google.gson.annotations.SerializedName;
import com.vechona.com.ui.model.Category;

import java.util.List;

public class TopCategoryResponseData {

    @SerializedName("topCategoryList")
    private List<Category> categoryList;

    public List<Category> getCategoryList() {
        return categoryList;
    }
}
