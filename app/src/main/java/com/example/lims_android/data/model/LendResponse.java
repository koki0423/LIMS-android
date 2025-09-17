package com.example.lims_android.data.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.Date;

public class LendResponse implements Serializable {
    @SerializedName("lend_ulid")
    private String lendUlid;

    @SerializedName("borrower_id")
    private String borrowerId;

    @SerializedName("lent_at")
    private Date lentAt;

    // Getterを定義
    public String getLendUlid() { return lendUlid; }
    public String getBorrowerId() { return borrowerId; }
    public Date getLentAt() { return lentAt; }
}