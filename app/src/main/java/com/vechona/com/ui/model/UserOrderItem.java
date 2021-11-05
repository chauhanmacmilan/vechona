package com.vechona.com.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserOrderItem implements Parcelable {

    @SerializedName("ORDER_ID")
    private String orderId;
    @SerializedName("USER_ID")
    private int userId;
    @SerializedName("SENDER_MOBILE_NUMBER")
    private String senderMobileNumber;
    @SerializedName("SENDER_NAME")
    private String senderName;
    @SerializedName("CUSTOMER_MOBILE_NUMBER")
    private String customerMobileNumber;
    @SerializedName("CUSTOMER_NAME")
    private String customerName;
    @SerializedName("CUSTOMER_ADDRESS")
    private String customerAddress;
    @SerializedName("ORDER_CREATION_DATE")
    private String orderCreationDate;
    @SerializedName("ORDER_DELIVERY_DATE")
    private String orderDeliveryDate;
    @SerializedName("TOTAL_ITEMS")
    private String totalItems;
    @SerializedName("TOTAL_AMOUNT")
    private String totalAmount;
    @SerializedName("COMISSION_AMOUNT")
    private String comissionAmount;
    @SerializedName("COMISSION_STATUS")
    private String comissionStatus;
    @SerializedName("PAYMENT_STATUS")
    private String paymentStatus;
    @SerializedName("PAYMENT_METHOD")
    private String paymentMethod;
    @SerializedName("SHIPPING_CHARGES")
    private String shippingCharges;
    @SerializedName("COD_CAHRGES")
    private String codCharges;
    @SerializedName("COD_AMOUNT_COLLECTION")
    private String codAmountCollection;
    @SerializedName("ORDER_STATUS")
    private String orderStatus;
    @SerializedName("CANCEL_REASON")
    private String cancelReason;
    @SerializedName("CANCEL_TIME")
    private String cancelTime;
    @SerializedName("ADDRESS_MODIFICATION_TIME")
    private String addressModificationTime;
    @SerializedName("TRACKING_NUMBER")
    private String trackingNumber;
    @SerializedName("COURIER_COMPANY_NAME")
    private String courierCompanyName;
    @SerializedName("TRACKING_LINK")
    private String trackingLink;
    public static final Creator<UserOrderItem> CREATOR = new Creator<UserOrderItem>() {
        @Override
        public UserOrderItem createFromParcel(Parcel in) {
            return new UserOrderItem(in);
        }

        @Override
        public UserOrderItem[] newArray(int size) {
            return new UserOrderItem[size];
        }
    };
    @SerializedName(value = "productList", alternate = {"orderItems"})
    private List<Product> productList;
    @SerializedName("transaction")
    private Transaction transaction;

    protected UserOrderItem(Parcel in) {
        orderId = in.readString();
        userId = in.readInt();
        senderMobileNumber = in.readString();
        senderName = in.readString();
        customerMobileNumber = in.readString();
        customerName = in.readString();
        customerAddress = in.readString();
        orderCreationDate = in.readString();
        orderDeliveryDate = in.readString();
        totalItems = in.readString();
        totalAmount = in.readString();
        comissionAmount = in.readString();
        comissionStatus = in.readString();
        paymentStatus = in.readString();
        paymentMethod = in.readString();
        shippingCharges = in.readString();
        codCharges = in.readString();
        codAmountCollection = in.readString();
        orderStatus = in.readString();
        cancelReason = in.readString();
        cancelTime = in.readString();
        addressModificationTime = in.readString();
        trackingNumber = in.readString();
        courierCompanyName = in.readString();
        trackingLink = in.readString();
        productList = in.createTypedArrayList(Product.CREATOR);
    }

    public String getOrderId() {
        return orderId;
    }

    public int getUserId() {
        return userId;
    }

    public String getSenderMobileNumber() {
        return senderMobileNumber;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getCustomerMobileNumber() {
        return customerMobileNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public String getOrderCreationDate() {
        return orderCreationDate;
    }

    public String getOrderDeliveryDate() {
        return orderDeliveryDate;
    }

    public String getTotalItems() {
        return totalItems;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getComissionAmount() {
        return comissionAmount;
    }

    public String getComissionStatus() {
        return comissionStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getShippingCharges() {
        return shippingCharges;
    }

    public String getCodCharges() {
        return codCharges;
    }

    public String getCodAmountCollection() {
        return codAmountCollection;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public String getCancelTime() {
        return cancelTime;
    }

    public String getAddressModificationTime() {
        return addressModificationTime;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public String getCourierCompanyName() {
        return courierCompanyName;
    }

    public String getTrackingLink() {
        return trackingLink;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public List<Product> getProductList() {
        return productList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(orderId);
        parcel.writeInt(userId);
        parcel.writeString(senderMobileNumber);
        parcel.writeString(senderName);
        parcel.writeString(customerMobileNumber);
        parcel.writeString(customerName);
        parcel.writeString(customerAddress);
        parcel.writeString(orderCreationDate);
        parcel.writeString(orderDeliveryDate);
        parcel.writeString(totalItems);
        parcel.writeString(totalAmount);
        parcel.writeString(comissionAmount);
        parcel.writeString(comissionStatus);
        parcel.writeString(paymentStatus);
        parcel.writeString(paymentMethod);
        parcel.writeString(shippingCharges);
        parcel.writeString(codCharges);
        parcel.writeString(codAmountCollection);
        parcel.writeString(orderStatus);
        parcel.writeString(cancelReason);
        parcel.writeString(cancelTime);
        parcel.writeString(addressModificationTime);
        parcel.writeString(trackingNumber);
        parcel.writeString(courierCompanyName);
        parcel.writeString(trackingLink);
        parcel.writeTypedList(productList);
    }
}
