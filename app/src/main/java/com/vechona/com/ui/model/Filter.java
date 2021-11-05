package com.vechona.com.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Filter implements Parcelable {

    public static final Creator<Filter> CREATOR = new Creator<Filter>() {
        @Override
        public Filter createFromParcel(Parcel source) {
            return new Filter(source);
        }

        @Override
        public Filter[] newArray(int size) {
            return new Filter[size];
        }
    };
    private List<FilterItem> filterItems;
    private boolean isMultiSelected;
    private String itemType;

    public Filter() {
    }

    protected Filter(Parcel in) {
        this.filterItems = new ArrayList<FilterItem>();
        in.readList(this.filterItems, FilterItem.class.getClassLoader());
        this.isMultiSelected = in.readByte() != 0;
        this.itemType = in.readString();
    }

    public List<FilterItem> getFilterItems() {
        return filterItems;
    }

    public void setFilterItems(List<FilterItem> filterItems) {
        this.filterItems = filterItems;
    }

    public boolean isMultiSelected() {
        return isMultiSelected;
    }

    public void setMultiSelected(boolean multiSelected) {
        isMultiSelected = multiSelected;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.filterItems);
        dest.writeByte(this.isMultiSelected ? (byte) 1 : (byte) 0);
        dest.writeString(this.itemType);
    }
}
