package com.example.lims_android.data.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.Date;

/**
 * APIから返される個々の資産（インスタンス）の情報をマッピングするためのデータクラス
 */
public class AssetResponse implements Serializable {

    @SerializedName("asset_id")
    private long assetId;

    @SerializedName("asset_master_id")
    private long assetMasterId;

    @SerializedName("management_number")
    private String managementNumber;

    @SerializedName("serial")
    private String serial;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("purchased_at")
    private Date purchasedAt;

    @SerializedName("status_id")
    private int statusId;

    @SerializedName("owner")
    private String owner;

    @SerializedName("default_location")
    private String defaultLocation;

    @SerializedName("location")
    private String location;

    @SerializedName("last_checked_at")
    private Date lastCheckedAt;

    @SerializedName("last_checked_by")
    private String lastCheckedBy;

    @SerializedName("notes")
    private String notes;

    // Getterメソッド
    public long getAssetId() { return assetId; }
    public long getAssetMasterId() { return assetMasterId; }
    public String getManagementNumber() { return managementNumber; }
    public String getSerial() { return serial; }
    public int getQuantity() { return quantity; }
    public Date getPurchasedAt() { return purchasedAt; }
    public int getStatusId() { return statusId; }
    public String getOwner() { return owner; }
    public String getDefaultLocation() { return defaultLocation; }
    public String getLocation() { return location; }
    public Date getLastCheckedAt() { return lastCheckedAt; }
    public String getLastCheckedBy() { return lastCheckedBy; }
    public String getNotes() { return notes; }
}