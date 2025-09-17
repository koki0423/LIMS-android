package com.example.lims_android.data.model;

// ユーザー情報を保持するシンプルなデータクラス
public class User {
    private final String name;
    private final String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}