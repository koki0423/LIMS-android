package com.example.lims_android.data.network;

import com.google.gson.annotations.SerializedName;

public class CreateLendRequest {
    @SerializedName("quantity")
    private final int quantity;

    @SerializedName("borrower_id")
    private final String borrowerId;

    // omitemptyのフィールドは、必要に応じてコンストラクタやセッターを追加
    // private String dueOn;
    // private String note;

    public CreateLendRequest(int quantity, String borrowerId) {
        this.quantity = quantity;
        this.borrowerId = borrowerId;
    }
}