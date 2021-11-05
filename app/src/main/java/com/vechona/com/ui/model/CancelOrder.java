package com.vechona.com.ui.model;

import com.google.gson.annotations.SerializedName;

public class CancelOrder {
    @SerializedName("ORDER_ID")
    private String orderId;
    @SerializedName("CANCEL_REASON")
    private String cancelReason;

    public CancelOrder(String orderId, String cancelReason) {
        this.orderId = orderId;
        this.cancelReason = cancelReason;
    }
}