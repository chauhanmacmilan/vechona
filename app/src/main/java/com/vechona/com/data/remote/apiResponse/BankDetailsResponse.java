package com.vechona.com.data.remote.apiResponse;

import com.google.gson.annotations.SerializedName;

public class BankDetailsResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private BankDetailsResponseData bankDetailsResponseData;

    public String getStatus() {
        return status;
    }

    public BankDetailsResponseData getBankDetailsResponseData() {
        return bankDetailsResponseData;
    }

    public class BankDetailsResponseData {
        @SerializedName("result")
        private String result;

        public String getResult() {
            return result;
        }
    }
}