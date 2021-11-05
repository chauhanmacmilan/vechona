package com.vechona.com.data.remote.apiResponse;

import com.google.gson.annotations.SerializedName;

public class UpdateBankDetailsResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private UpdateBankDetailsResponseData updateBankDetailsResponseData;

    public String getStatus() {
        return status;
    }

    public UpdateBankDetailsResponseData getUpdateBankDetailsResponseData() {
        return updateBankDetailsResponseData;
    }

    public class UpdateBankDetailsResponseData {
        @SerializedName("result")
        private String result;

        public String getResult() {
            return result;
        }
    }
}
