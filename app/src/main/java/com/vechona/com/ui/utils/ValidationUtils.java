package com.vechona.com.ui.utils;

public class ValidationUtils {

    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.length() == 10;
    }

    public static boolean isValidPincode(String pincode) {
        return pincode.length() == 6;
    }

}
