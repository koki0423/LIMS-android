package com.example.lims_android.data.model;

import java.io.Serializable;

// Serializableを実装してFragment間で渡せるようにする
public class Asset implements Serializable {
    private final String barcode;
    private final String name;
    private final String description;

    public Asset(String barcode, String name, String description) {
        this.barcode = barcode;
        this.name = name;
        this.description = description;
    }

    public String getBarcode() { return barcode; }
    public String getName() { return name; }
    public String getDescription() { return description; }
}