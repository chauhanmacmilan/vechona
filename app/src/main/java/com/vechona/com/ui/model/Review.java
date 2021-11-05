package com.vechona.com.ui.model;

import com.google.gson.annotations.SerializedName;

public class Review {

    @SerializedName("REVIEW_ID")
    private int reviewId;

    @SerializedName("USER_ID")
    private int userId;

    @SerializedName("ORDER_ID")
    private String orderId;

    @SerializedName("PRODUCT_ID")
    private int productId;

    @SerializedName("RATING")
    private int rating;

    @SerializedName("COMMENT")
    private String comment;

    @SerializedName("VISIBILITY")
    private boolean visibility;

    @SerializedName("REVIEW_CREATION_DATE")
    private String reviewCreationDate;

    public Review(int userId, String orderId, int productId, int rating, String comment) {
        this.userId = userId;
        this.orderId = orderId;
        this.productId = productId;
        this.rating = rating;
        this.comment = comment;
    }

    public int getReviewId() {
        return reviewId;
    }

    public int getUserId() {
        return userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public int getProductId() {
        return productId;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public String getReviewCreationDate() {
        return reviewCreationDate;
    }
}
