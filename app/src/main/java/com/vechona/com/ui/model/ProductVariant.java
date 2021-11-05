package com.vechona.com.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ProductVariant implements Parcelable {

    public static final Creator<ProductVariant> CREATOR = new Creator<ProductVariant>() {
        @Override
        public ProductVariant createFromParcel(Parcel source) {
            return new ProductVariant(source);
        }

        @Override
        public ProductVariant[] newArray(int size) {
            return new ProductVariant[size];
        }
    };
    @SerializedName("PRODUCT_ID")
    private int productId;
    @SerializedName("VARIANT_NAME")
    private String variantName;
    @SerializedName("VARIANT_VALUE")
    private String variantValue;

    protected ProductVariant(Parcel in) {
        this.productId = in.readInt();
        this.variantName = in.readString();
        this.variantValue = in.readString();
    }

    public int getProductId() {
        return productId;
    }

    public String getVariantName() {
        return variantName;
    }

    public String getVariantValue() {
        return variantValue;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.productId);
        dest.writeString(this.variantName);
        dest.writeString(this.variantValue);
    }
}
