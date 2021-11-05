package com.vechona.com.ui.model;

import com.google.gson.annotations.SerializedName;

public class Transaction {

    @SerializedName("ORDERID")
    private String orderId;
    @SerializedName("TXNID")
    private String txnId;
    @SerializedName("CUST_ID")
    private String custId;
    @SerializedName("TXNAMOUNT")
    private String txnAmount;
    @SerializedName("STATUS")
    private String status;
    @SerializedName("RESPCODE")
    private String respCode;
    @SerializedName("RESPMSG")
    private String respMsg;
    @SerializedName("TXNDATE")
    private String txnDate;
    @SerializedName("BANKNAME")
    private String bankName;
    @SerializedName("PAYMENTMODE")
    private String paymentMode;
    @SerializedName("CHECKSUMHASH")
    private String checkSumHash;
    @SerializedName("BIN_NUMBER")
    private String binNumber;
    @SerializedName("CARD_LAST_NUMS")
    private String cardLastNums;
}
