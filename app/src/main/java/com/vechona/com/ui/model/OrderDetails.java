package com.vechona.com.ui.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetails {
    @SerializedName("ORDER_ID")
    @Expose
    private String orderId;

    public OrderDetails(String orderId) {
        this.orderId = orderId;
    }
}
