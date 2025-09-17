package com.example.lims_android.data.network;

import com.google.gson.annotations.SerializedName;

public class CreateReturnRequest {
    @SerializedName("quantity")
    private final int quantity;

    @SerializedName("processed_by_id")
    private final String processedById;

    public CreateReturnRequest(int quantity, String processedById) {
        this.quantity = quantity;
        this.processedById = processedById;
    }
}