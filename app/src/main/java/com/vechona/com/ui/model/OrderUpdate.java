package com.vechona.com.ui.model;

import com.google.gson.annotations.SerializedName;

public class OrderUpdate {

    @SerializedName("STATUS")
    private String status;
    @SerializedName("BANKNAME")
    private String bankName;
    @SerializedName("TXNDATE")
    private String txnDate;
    @SerializedName("TXNID")
    private String txnId;
    @SerializedName("RESPCODE")
    private String repsCode;
    @SerializedName("PAYMENTMODE")
    private String paymentMode;
    @SerializedName("BANKTXNID")
    private String bankTxnId;
    @SerializedName("CURRENCY")
    private String currency;
    @SerializedName("GATEWAYNAME")
    private String gateWayName;
    @SerializedName("RESPMSG")
    private String repsMsg;
    @SerializedName("CHECKSUMHASH")
    private String checkSumHash;
    @SerializedName("ORDERID")
    private String orderId;
    @SerializedName("TXNAMOUNT")
    private String txnAmount;
    @SerializedName("MID")
    private String mid;
    @SerializedName("USER_ID")
    private int userId;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setTxnDate(String txnDate) {
        this.txnDate = txnDate;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public void setRepsCode(String repsCode) {
        this.repsCode = repsCode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public void setBankTxnId(String bankTxnId) {
        this.bankTxnId = bankTxnId;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setGateWayName(String gateWayName) {
        this.gateWayName = gateWayName;
    }

    public void setRepsMsg(String repsMsg) {
        this.repsMsg = repsMsg;
    }

    public void setCheckSumHash(String checkSumHash) {
        this.checkSumHash = checkSumHash;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setTxnAmount(String txnAmount) {
        this.txnAmount = txnAmount;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
