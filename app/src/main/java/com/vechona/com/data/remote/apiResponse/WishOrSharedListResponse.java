package com.vechona.com.data.remote.apiResponse;

import com.google.gson.annotations.SerializedName;

public class WishOrSharedListResponse {

    @SerializedName("status")
    private String Status;

    @SerializedName("data")
    private Data data;

    public String getStatus() {
        return Status;
    }

    public Data getData() {
        return data;
    }

    public class Data {
        @SerializedName("result")
        private String result;

        public String getResult() {
            return result;
        }
    }
}