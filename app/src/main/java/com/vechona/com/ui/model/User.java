package com.vechona.com.ui.model;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("USER_ID")
    private int userId;

    @SerializedName("MOBILE_NUMBER")
    private String phoneNumber;

    @SerializedName("FIREBASE_ID")
    private String firebaseUserId;

    @SerializedName("FIRST_NAME")
    private String firstName;

    @SerializedName("LAST_NAME")
    private String lastName;

    @SerializedName("EMAIL_ID")
    private String email;

    @SerializedName("ADDRESS1")
    private String address1;

    @SerializedName("ADDRESS2")
    private String address2;

    @SerializedName("CITY")
    private String city;

    @SerializedName("STATE")
    private String state;

    @SerializedName("COUNTRY")
    private String country;

    @SerializedName("PINCODE")
    private String pincode;

    @SerializedName("accountDetails")
    private BankAccountDetails accountDetails;

    public int getUserID() {
        return userId;
    }

    public void setUserID(int userID) {
        this.userId = userID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirebaseUserId() {
        return firebaseUserId;
    }

    public void setFirebaseUserId(String firebaseUserId) {
        this.firebaseUserId = firebaseUserId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public BankAccountDetails getAccountDetails() {
        return accountDetails;
    }

    public void setAccountDetails(BankAccountDetails accountDetails) {
        this.accountDetails = accountDetails;
    }
}
