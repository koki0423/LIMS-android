package com.example.lims_android.ui.search;

import androidx.annotation.NonNull;

public class Category {
    private final int id;
    private final String displayName;
    private final String apiCode;

    public Category(int id, String displayName, String apiCode) {
        this.id = id;
        this.displayName = displayName;
        this.apiCode = apiCode;
    }

    public int getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getApiCode() {
        return apiCode;
    }

    // ArrayAdapterは、このtoString()メソッドの返り値をスピナーに表示します
    @NonNull
    @Override
    public String toString() {
        return displayName;
    }
}