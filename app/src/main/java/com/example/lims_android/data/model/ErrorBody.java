package com.example.lims_android.data.model;

import com.google.gson.annotations.SerializedName;

// JSONの最も外側の "error" オブジェクトに対応
public class ErrorBody {
    @SerializedName("error")
    private ErrorDetails error;

    public ErrorDetails getError() {
        return error;
    }
}