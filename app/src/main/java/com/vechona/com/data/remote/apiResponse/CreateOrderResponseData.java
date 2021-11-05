package com.vechona.com.data.remote.apiResponse;

import com.google.gson.annotations.SerializedName;
import com.vechona.com.ui.model.OrderResponse;

public class CreateOrderResponseData {

    @SerializedName("order")
    private OrderResponse orderResponse;

    public OrderResponse getOrderResponse() {
        return orderResponse;
    }
}
