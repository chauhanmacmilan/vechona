package com.vechona.com.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SenderDetailsItem implements Parcelable {
    public static final Creator<SenderDetailsItem> CREATOR = new Creator<SenderDetailsItem>() {
        @Override
        public SenderDetailsItem createFromParcel(Parcel in) {
            return new SenderDetailsItem(in);
        }

        @Override
        public SenderDetailsItem[] newArray(int size) {
            return new SenderDetailsItem[size];
        }
    };
    private String name, phoneNumber;
    private boolean isSelected;

    public SenderDetailsItem(String name, String phoneNo) {
        this.name = name;
        this.phoneNumber = phoneNo;
    }

    protected SenderDetailsItem(Parcel in) {
        name = in.readString();
        phoneNumber = in.readString();
        isSelected = in.readByte() != 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(phoneNumber);
        parcel.writeByte((byte) (isSelected ? 1 : 0));
    }
}