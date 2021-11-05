package com.vechona.com.ui.model;

import com.google.gson.annotations.SerializedName;

public class CreateOrder {

    @SerializedName("order")
    private CreateOrderItem createOrderItem;

    public CreateOrder(CreateOrderItem createOrderItem) {
        this.createOrderItem = createOrderItem;
    }
}
