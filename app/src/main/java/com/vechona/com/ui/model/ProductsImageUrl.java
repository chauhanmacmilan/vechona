package com.vechona.com.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ProductsImageUrl implements Parcelable {

    public static final Creator<ProductsImageUrl> CREATOR = new Creator<ProductsImageUrl>() {
        @Override
        public ProductsImageUrl createFromParcel(Parcel source) {
            return new ProductsImageUrl(source);
        }

        @Override
        public ProductsImageUrl[] newArray(int size) {
            return new ProductsImageUrl[size];
        }
    };
    @SerializedName("IMAGE_URL")
    private String imageUrl;
    @SerializedName("PRODUCT_ID")
    private int productId;

    protected ProductsImageUrl(Parcel in) {
        this.imageUrl = in.readString();
        this.productId = in.readInt();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getProductId() {
        return productId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageUrl);
        dest.writeInt(this.productId);
    }
}
