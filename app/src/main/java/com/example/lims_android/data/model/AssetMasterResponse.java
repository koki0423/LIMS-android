package com.example.lims_android.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

// Serializableを実装してFragment間で渡せるようにする
public class AssetMasterResponse implements java.io.Serializable {
    @SerializedName("asset_master_id")
    private int assetMasterId;

    @SerializedName("management_number")
    private String managementNumber;

    @SerializedName("name")
    private String name;

   @SerializedName("management_category_id")
    private int managementCategoryId;
    @SerializedName("genre_id")
    private int genreId;
    @SerializedName("manufacturer")
    private String manufacturer;
    @SerializedName("model")
    private String model;
    @SerializedName("created_at")
    private Date createdAt;

    // Getterメソッドを定義
    public long getAssetMasterId() { return assetMasterId; }
    public String getManagementNumber() { return managementNumber; }
    public String getName() { return name; }
    public int getManagementCategoryId() { return managementCategoryId; }
    public int getGenreId() { return genreId; }
    public String getManufacturer() { return manufacturer; }
    public String getModel() { return model; }
    public Date getCreatedAt() { return createdAt; }
}