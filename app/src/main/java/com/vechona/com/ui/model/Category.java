package com.vechona.com.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Category implements Parcelable {

    @SerializedName("CATEGORY_ID")
    private int categoryId;

    @SerializedName("CATEGORY_NAME")
    private String categoryName;

    @SerializedName("IMAGE_URL")
    private String imageUrl;
    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
    @SerializedName("SHIPPING_CHARGES")
    private int shippingCharges;
    @SerializedName("APPLY_SHIPPING_CHARGES")
    private boolean applyShippingCharges;

    protected Category(Parcel in) {
        categoryId = in.readInt();
        categoryName = in.readString();
        imageUrl = in.readString();
        shippingCharges = in.readInt();
        applyShippingCharges = in.readByte() != 0;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getShippingCharges() {
        return shippingCharges;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(categoryId);
        parcel.writeString(categoryName);
        parcel.writeString(imageUrl);
        parcel.writeInt(shippingCharges);
        parcel.writeByte((byte) (applyShippingCharges ? 1 : 0));
    }
}
