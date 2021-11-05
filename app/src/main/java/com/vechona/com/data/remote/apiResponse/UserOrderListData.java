package com.vechona.com.data.remote.apiResponse;

import com.google.gson.annotations.SerializedName;
import com.vechona.com.ui.model.UserOrderItem;

import java.util.List;

public class UserOrderListData {

    @SerializedName("orderList")
    private List<UserOrderItem> userOrderItems;

    public List<UserOrderItem> getUserOrderItems() {
        return userOrderItems;
    }
}
