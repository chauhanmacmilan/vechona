package com.vechona.com.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ShippingAddressItem implements Parcelable {
    public static final Creator<ShippingAddressItem> CREATOR = new Creator<ShippingAddressItem>() {
        @Override
        public ShippingAddressItem createFromParcel(Parcel in) {
            return new ShippingAddressItem(in);
        }

        @Override
        public ShippingAddressItem[] newArray(int size) {
            return new ShippingAddressItem[size];
        }
    };
    private String name, address, phoneNumber;
    private boolean isSelected;

    public ShippingAddressItem() {
    }

    public ShippingAddressItem(String name, String address, String phoneNumber) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }


    private ShippingAddressItem(Parcel in) {
        name = in.readString();
        address = in.readString();
        phoneNumber = in.readString();
        isSelected = in.readByte() != 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
        parcel.writeString(address);
        parcel.writeString(phoneNumber);
        parcel.writeByte((byte) (isSelected ? 1 : 0));
    }
}