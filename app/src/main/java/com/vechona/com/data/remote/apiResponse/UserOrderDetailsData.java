package com.vechona.com.data.remote.apiResponse;

import com.google.gson.annotations.SerializedName;
import com.vechona.com.ui.model.UserOrderItem;

public class UserOrderDetailsData {

    @SerializedName("orderDetails")
    private UserOrderItem userOrderItem;

    public UserOrderItem getUserOrderItem() {
        return userOrderItem;
    }
}
