package com.vechona.com.ui.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DeleteCart {
    @SerializedName("items")
    @Expose
    private List<CartItem> cartItems;

    public DeleteCart(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }
}