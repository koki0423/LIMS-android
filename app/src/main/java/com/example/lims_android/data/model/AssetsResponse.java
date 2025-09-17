package com.example.lims_android.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class AssetsResponse {
    @SerializedName("items")
    private List<AssetResponse> items; // AssetResponseは以前作成したもの
    public List<AssetResponse> getItems() { return items; }
}