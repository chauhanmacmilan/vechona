package com.vechona.com.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Catalog implements Parcelable {

    @SerializedName("CATALOG_ID")
    private int catalogId;
    @SerializedName("CATALOG_NAME")
    private String catalogName;
    @SerializedName("CATALOG_DESCRIPTION")
    private String catalogDescription;
    @SerializedName("CATEGORY_NAME")
    private String categoryName;
    @SerializedName("CATALOG_IMAGE_URL")
    private String catalogImageUrl;
    private int USER_ID;
    @SerializedName("productList")
    private List<Product> products;
    @SerializedName("catalog_variant")
    private CatalogVariant catalogVariant;
    @SerializedName("CATALOG_BASE_PRICE")
    private int catalogBasePrice;
    @SerializedName("AVERAGE_RATINGS")
    private String averageRating;
    @SerializedName("ORDER_COUNT")
    private String orderCount;
    @SerializedName("CATALOG_CREATION_DATE")
    private String catalogCreationDate;
    public static final Creator<Catalog> CREATOR = new Creator<Catalog>() {
        @Override
        public Catalog createFromParcel(Parcel source) {
            return new Catalog(source);
        }

        @Override
        public Catalog[] newArray(int size) {
            return new Catalog[size];
        }
    };
    @SerializedName("IS_PREPAID")
    private boolean is_prepaid;
    @SerializedName("PREPAID_SHIPPING_DATE")
    private String preShippingDate;


    public int getCatalogId() {
        return catalogId;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCatalogImageUrl() {
        return catalogImageUrl;
    }

    public String getCatalogDescription() {
        return catalogDescription;
    }

    public List<Product> getProducts() {
        return products;
    }

    public int getCatalogBasePrice() {
        return catalogBasePrice;
    }

    public int getUSER_ID() {
        return USER_ID;
    }

    public CatalogVariant getCatalogVariant() {
        return catalogVariant;
    }

    public String getAverageRating() {
        if (averageRating == null)
            return "0";
        return averageRating;
    }

    public String getOrderCount() {
        if (orderCount == null)
            return "0";
        return orderCount;
    }

    public String getCatalogCreationDate() {
        return catalogCreationDate;
    }

    public Catalog(int userId, int catalogId) {
        this.USER_ID = userId;
        this.catalogId = catalogId;
    }

    protected Catalog(Parcel in) {
        this.catalogId = in.readInt();
        this.catalogName = in.readString();
        this.catalogDescription = in.readString();
        this.categoryName = in.readString();
        this.catalogImageUrl = in.readString();
        this.USER_ID = in.readInt();
        this.products = in.createTypedArrayList(Product.CREATOR);
        this.catalogVariant = in.readParcelable(CatalogVariant.class.getClassLoader());
        this.catalogBasePrice = in.readInt();
        this.averageRating = in.readString();
        this.orderCount = in.readString();
        this.catalogCreationDate = in.readString();
        this.is_prepaid = in.readByte() != 0;
        this.preShippingDate = in.readString();
    }

    public boolean is_prepaid() {
        return is_prepaid;
    }

    public String getPreShippingDate() {
        return preShippingDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.catalogId);
        dest.writeString(this.catalogName);
        dest.writeString(this.catalogDescription);
        dest.writeString(this.categoryName);
        dest.writeString(this.catalogImageUrl);
        dest.writeInt(this.USER_ID);
        dest.writeTypedList(this.products);
        dest.writeParcelable(this.catalogVariant, flags);
        dest.writeInt(this.catalogBasePrice);
        dest.writeString(this.averageRating);
        dest.writeString(this.orderCount);
        dest.writeString(this.catalogCreationDate);
        dest.writeByte(this.is_prepaid ? (byte) 1 : (byte) 0);
        dest.writeString(this.preShippingDate);
    }

}