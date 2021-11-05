package com.vechona.com.data.remote.apiResponse;

import com.google.gson.annotations.SerializedName;
import com.vechona.com.ui.model.HomeSlider;

import java.util.List;

public class HomeSliderResponseData {

    @SerializedName("sliderList")
    private List<HomeSlider> sliderList;

    public List<HomeSlider> getSliderList() {
        return sliderList;
    }
}
