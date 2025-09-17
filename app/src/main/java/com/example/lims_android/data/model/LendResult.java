package com.example.lims_android.data.model;

public class LendResult {
    public final boolean isSuccess;
    public final String message;

    public LendResult(boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }
}