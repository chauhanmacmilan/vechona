package com.vechona.com.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class GetPincodeAvailablePrice{
    @SerializedName("status")
    public String status;
    @SerializedName("data")
    public Data data;
    public class Data {
        @SerializedName("SC_ID")
        public int SC_ID;
        @SerializedName("COURIER_ID")
        public int COURIER_ID;
        @SerializedName("MIN_WEIGHT")
        public int MIN_WEIGHT;
        @SerializedName("MAX_WEIGHT")
        public int MAX_WEIGHT;
        @SerializedName("PRICE")
        public int PRICE;

        public int getSC_ID() {
            return SC_ID;
        }

        public void setSC_ID(int SC_ID) {
            this.SC_ID = SC_ID;
        }

        public int getCOURIER_ID() {
            return COURIER_ID;
        }

        public void setCOURIER_ID(int COURIER_ID) {
            this.COURIER_ID = COURIER_ID;
        }

        public int getMIN_WEIGHT() {
            return MIN_WEIGHT;
        }

        public void setMIN_WEIGHT(int MIN_WEIGHT) {
            this.MIN_WEIGHT = MIN_WEIGHT;
        }

        public int getMAX_WEIGHT() {
            return MAX_WEIGHT;
        }

        public void setMAX_WEIGHT(int MAX_WEIGHT) {
            this.MAX_WEIGHT = MAX_WEIGHT;
        }

        public int getPRICE() {
            return PRICE;
        }

        public void setPRICE(int PRICE) {
            this.PRICE = PRICE;
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
