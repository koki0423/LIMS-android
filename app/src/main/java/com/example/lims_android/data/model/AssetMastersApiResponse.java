package com.example.lims_android.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class AssetMastersApiResponse {
    @SerializedName("items")
    private List<AssetMasterResponse> items;
    public List<AssetMasterResponse> getItems() { return items; }
}