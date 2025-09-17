package com.example.lims_android.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;

@Singleton
public class SettingsManager {

    private static final String PREF_NAME = "app_settings";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_API_URL = "api_url";

    private final SharedPreferences sharedPreferences;

    @Inject
    public SettingsManager(@ApplicationContext Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // --- ユーザー名 ---
    public void saveUserName(String name) {
        sharedPreferences.edit().putString(KEY_USER_NAME, name).apply();
    }

    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, "ゲスト"); // デフォルト値
    }

    // --- APIサーバーアドレス ---
    public void saveApiUrl(String url) {
        sharedPreferences.edit().putString(KEY_API_URL, url).apply();
    }

    public String getApiUrl() {
        String url = sharedPreferences.getString(KEY_API_URL, "https://api.example.com/");
        // URLの末尾が "/" で終わっているか確認し、終わっていなければ追加する
        if (url != null && !url.endsWith("/")) {
            return url + "/";
        }
        return url;
    }
}