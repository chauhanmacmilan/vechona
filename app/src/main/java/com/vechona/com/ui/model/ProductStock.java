package com.vechona.com.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ProductStock implements Parcelable {

    public static final Creator<ProductStock> CREATOR = new Creator<ProductStock>() {
        @Override
        public ProductStock createFromParcel(Parcel source) {
            return new ProductStock(source);
        }

        @Override
        public ProductStock[] newArray(int size) {
            return new ProductStock[size];
        }
    };
    @SerializedName("STOCK_ID")
    private int stockId;
    @SerializedName("PRODUCT_ID")
    private int productId;
    @SerializedName("SIZE")
    private String size;
    @SerializedName("COLOR")
    private String color;
    @SerializedName("WEIGHT")
    private int weight;
    @SerializedName("QUANTITY")
    private int quantity;

    protected ProductStock(Parcel in) {
        this.stockId = in.readInt();
        this.productId = in.readInt();
        this.size = in.readString();
        this.color = in.readString();
        this.weight = in.readInt();
        this.quantity = in.readInt();
    }

    public int getStockId() {
        return stockId;
    }

    public int getProductId() {
        return productId;
    }

    public String getSize() {
        return size;
    }

    public String getColor() {
        return color;
    }

    public int getWeight() {
        return weight;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.stockId);
        dest.writeInt(this.productId);
        dest.writeString(this.size);
        dest.writeString(this.color);
        dest.writeInt(this.weight);
        dest.writeInt(this.quantity);
    }
}
