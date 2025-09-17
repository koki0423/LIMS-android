package com.example.lims_android.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class LendsApiResponse {

    @SerializedName("items")
    private List<LendResponse> items;

    // 他のフィールド(totalなど)も必要なら追加

    public List<LendResponse> getItems() {
        return items;
    }
}