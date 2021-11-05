package com.vechona.com.ui.model;

import com.google.gson.annotations.SerializedName;

public class CartItem {

    @SerializedName("USER_ID")
    private int userId;
    @SerializedName("PRODUCT_ID")
    private int productId;
    @SerializedName("STOCK_ID")
    private int stockId;
    @SerializedName("CATALOG_ID")
    private int catalogId;
    @SerializedName("QUANTITY")
    private int quantity;
    @SerializedName("ITEM_ADDED_DATE")
    private String addedDate;
    @SerializedName("product")
    private Product product;
    @SerializedName("product_stock")
    private ProductStock stock;
    @SerializedName("category")
    private Category category;
    @SerializedName("WEIGHT")
    private String weight;
    @SerializedName("UNIT")
    private String unit;

    private int price;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductStock getStock() {
        if (stock == null)
            return product.getProductStockOfId(stockId);
        return stock;
    }

    public void setStock(ProductStock stock) {
        this.stock = stock;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    public int getPrice() {
        return price;
    }


    public void setPrice(int price) {
        this.price = price;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(int catalogId) {
        this.catalogId = catalogId;
    }

    public Category getCategory() {
        if (category == null) {
            return product.getCategory();
        }
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
