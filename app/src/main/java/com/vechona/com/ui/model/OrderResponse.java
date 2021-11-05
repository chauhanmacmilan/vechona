package com.vechona.com.ui.model;

import com.google.gson.annotations.SerializedName;

public class OrderResponse {

    @SerializedName("MID")
    private String mid;

    @SerializedName("ORDER_ID")
    private String orderId;

    @SerializedName("CUST_ID")
    private String custId;

    @SerializedName("INDUSTRY_TYPE_ID")
    private String industryTypeId;

    @SerializedName("CHANNEL_ID")
    private String channelId;

    @SerializedName("TXN_AMOUNT")
    private String txnAmount;

    @SerializedName("WEBSITE")
    private String website;

    @SerializedName("CALLBACK_URL")
    private String callbackUrl;

    @SerializedName("CHECKSUMHASH")
    private String checkSumHash;

    public String getMid() {
        return mid;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustId() {
        return custId;
    }

    public String getIndustryTypeId() {
        return industryTypeId;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getTxnAmount() {
        return txnAmount;
    }

    public String getWebsite() {
        return website;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public String getCheckSumHash() {
        return checkSumHash;
    }
}
