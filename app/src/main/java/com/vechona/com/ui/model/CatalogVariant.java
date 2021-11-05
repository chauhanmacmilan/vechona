package com.vechona.com.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CatalogVariant implements Parcelable {

    @SerializedName("CATALOG_ID")
    private int catalogId;
    @SerializedName("VARIANT_NAME")
    private String varientName;
    @SerializedName("VARIANT_VALUE")
    private String variantValue;

    public static final Creator<CatalogVariant> CREATOR = new Creator<CatalogVariant>() {
        @Override
        public CatalogVariant createFromParcel(Parcel source) {
            return new CatalogVariant(source);
        }

        @Override
        public CatalogVariant[] newArray(int size) {
            return new CatalogVariant[size];
        }
    };

    protected CatalogVariant(Parcel in) {
        this.catalogId = in.readInt();
        this.varientName = in.readString();
        this.variantValue = in.readString();
    }

    public int getCatalogId() {
        return catalogId;
    }

    public String getVarientName() {
        return varientName;
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
        dest.writeInt(this.catalogId);
        dest.writeString(this.varientName);
        dest.writeString(this.variantValue);
    }
}
