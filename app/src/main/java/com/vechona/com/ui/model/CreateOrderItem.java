package com.vechona.com.ui.model;

import com.google.gson.annotations.SerializedName;

public class CreateOrderItem {
    @SerializedName("SENDER_MOBILE_NUMBER")
    String senderMobileNumber;
    @SerializedName("SENDER_NAME")
    String senderName;
    @SerializedName("USER_ID")
    private int userId;
    @SerializedName("TOTAL_AMOUNT")
    private int totalAmount;
    @SerializedName("CUSTOMER_MOBILE_NUMBER")
    private String customerMobileNumber;
    @SerializedName("CUSTOMER_NAME")
    private String customerName;
    @SerializedName("CUSTOMER_ADDRESS")
    private String customerAddress;
    @SerializedName("COMISSION_AMOUNT")
    private int comissionAmount;
    @SerializedName("PAYMENT_METHOD")
    private String paymentMethod;
    @SerializedName("SHIPPING_CHARGES")
    private int shippingCharges;
    @SerializedName("COD_CAHRGES")
    private int codCharges;

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setSenderMobileNumber(String senderMobileNumber) {
        this.senderMobileNumber = senderMobileNumber;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setCustomerMobileNumber(String customerMobileNumber) {
        this.customerMobileNumber = customerMobileNumber;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public void setComissionAmount(int comissionAmount) {
        this.comissionAmount = comissionAmount;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setShippingCharges(int shippingCharges) {
        this.shippingCharges = shippingCharges;
    }

    public int getCodCharges() {
        return codCharges;
    }

    public void setCodCharges(int codCharges) {
        this.codCharges = codCharges;
    }
}