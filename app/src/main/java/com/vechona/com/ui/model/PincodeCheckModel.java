package com.vechona.com.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PincodeCheckModel implements Parcelable {
    @SerializedName("pincode")
    private int pincode;
    @SerializedName("weight")
    private int weight;
    @SerializedName("type")
    private String type;

    public PincodeCheckModel(int pincode, int weight, String type) {
        this.pincode = pincode;
        this.weight = weight;
        this.type = type;
    }

    protected PincodeCheckModel(Parcel in) {
        pincode = in.readInt();
        weight = in.readInt();
        type = in.readString();
    }

    public static final Creator<PincodeCheckModel> CREATOR = new Creator<PincodeCheckModel>() {
        @Override
        public PincodeCheckModel createFromParcel(Parcel in) {
            return new PincodeCheckModel(in);
        }

        @Override
        public PincodeCheckModel[] newArray(int size) {
            return new PincodeCheckModel[size];
        }
    };



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.pincode);
        dest.writeInt(this.weight);
        dest.writeString(this.type);
    }
}
