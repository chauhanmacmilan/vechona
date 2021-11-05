package com.vechona.com.data.remote.apiResponse;

import com.google.gson.annotations.SerializedName;
import com.vechona.com.ui.model.UserOrderItem;

public class OrderDetailsResponseData {

    @SerializedName("orderDetails")
    private UserOrderItem orderDetails;

    public UserOrderItem getOrderDetails() {
        return orderDetails;
    }

}
