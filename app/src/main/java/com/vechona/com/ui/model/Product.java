package com.vechona.com.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Product implements Parcelable {

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
    @SerializedName("PRODUCT_ID")
    private int productId;
    @SerializedName("SKU_ID")
    private String skuId;
    @SerializedName("PRODUCT_NAME")
    private String productName;
    @SerializedName("CATEGORY_NAME")
    private String categoryName;
    @SerializedName("PRODUCT_DESCRIPTION")
    private String productDescription;
    @SerializedName("PRICE")
    private int price;
    @SerializedName("WEIGHT")
    private String weight;
    @SerializedName("UNIT")
    private String unit;
    @SerializedName("VENDOR")
    private String vender;
    @SerializedName("DISCOUNT")
    private double discount;
    @SerializedName("product_image_urls")
    private List<ProductsImageUrl> productsImageUrls;
    @SerializedName("product_variants")
    private List<ProductVariant> productVariants;
    @SerializedName("product_stocks")
    private List<ProductStock> productStocks;
    @SerializedName("product_reviews")
    private List<ProductReview> productReviews;

    @SerializedName("PRODUCT_CREATION_DATE")
    private String productCreationDate;
    @SerializedName("STATUS")
    private String status;
    @SerializedName("SIZE")
    private String size;
    @SerializedName("COLOR")
    private String color;
    @SerializedName("QUANTITY")
    private String quantity;
    @SerializedName("category")
    private Category category;
    private int productCatalogId;

    protected Product(Parcel in) {
        productId = in.readInt();
        skuId = in.readString();
        productName = in.readString();
        categoryName = in.readString();
        productDescription = in.readString();
        price = in.readInt();
        weight = in.readString();
        unit = in.readString();
        vender = in.readString();
        discount = in.readDouble();
        productsImageUrls = in.createTypedArrayList(ProductsImageUrl.CREATOR);
        productVariants = in.createTypedArrayList(ProductVariant.CREATOR);
        productStocks = in.createTypedArrayList(ProductStock.CREATOR);
        productCreationDate = in.readString();
        status = in.readString();
        size = in.readString();
        color = in.readString();
        quantity = in.readString();
        category = in.readParcelable(Category.class.getClassLoader());
        productCatalogId = in.readInt();
    }

    public Category getCategory() {
        return category;
    }

    public int getProductCatalogId() {
        return productCatalogId;
    }

    public void setProductCatalogId(int productCatalogId) {
        this.productCatalogId = productCatalogId;
    }

    public int getProductId() {
        return productId;
    }

    public String getSkuId() {
        return skuId;
    }

    public String getProductName() {
        return productName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public int getPrice() {
        return price;
    }

    public String getWeight() {
        return weight;
    }

    public String getUnit() {
        return unit;
    }

    public String getVender() {
        return vender;
    }

    public double getDiscount() {
        return discount;
    }

    public String getProductCreationDate() {
        return productCreationDate;
    }

    public String getStatus() {
        return status;
    }

    public String getSize() {
        return size;
    }

    public String getColor() {
        return color;
    }

    public String getQuantity() {
        return quantity;
    }

    public List<ProductsImageUrl> getProductsImageUrls() {
        return productsImageUrls;
    }

    public List<ProductVariant> getProductVariants() {
        return productVariants;
    }

    public List<ProductStock> getProductStockList() {
        return productStocks;
    }

    public ProductStock getProductStockOfId(int stockId) {
        for (ProductStock productStock : productStocks) {
            if (productStock.getStockId() == stockId) {
                return productStock;
            }
        }
        return null;
    }

    public Map<String, Map<String, ProductStock>> getProductStocks() {

        Map<String, Map<String, ProductStock>> stockMap = new HashMap<>();
        for (ProductStock stock : productStocks) {
            if (!stockMap.containsKey(stock.getSize())) {
                Map<String, ProductStock> map = new HashMap<>();
                for (ProductStock stock1 : productStocks) {
                    if (stock.getSize().equals(stock1.getSize()) && !map.containsKey(stock.getColor())) {
                        map.put(stock.getColor(), stock);
                    }
                }
                stockMap.put(stock.getSize(), map);
            }
        }
        return stockMap;
    }

    public List<ProductReview> getProductReviews() {
        return productReviews;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(productId);
        parcel.writeString(skuId);
        parcel.writeString(productName);
        parcel.writeString(categoryName);
        parcel.writeString(productDescription);
        parcel.writeInt(price);
        parcel.writeString(weight);
        parcel.writeString(unit);
        parcel.writeString(vender);
        parcel.writeDouble(discount);
        parcel.writeTypedList(productsImageUrls);
        parcel.writeTypedList(productVariants);
        parcel.writeTypedList(productStocks);
        parcel.writeString(productCreationDate);
        parcel.writeString(status);
        parcel.writeString(size);
        parcel.writeString(color);
        parcel.writeString(quantity);
        parcel.writeParcelable(category, i);
        parcel.writeInt(productCatalogId);
    }
}
