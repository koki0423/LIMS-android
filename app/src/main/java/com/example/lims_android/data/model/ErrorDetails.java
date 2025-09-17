package com.example.lims_android.data.model;

import com.google.gson.annotations.SerializedName;

// "error" オブジェクトの内部に対応
public class ErrorDetails {
    @SerializedName("code")
    private String code;

    @SerializedName("message")
    private String message;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}