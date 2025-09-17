package com.example.lims_android.data.repository;

import android.os.Handler;
import android.os.Looper;

import com.example.lims_android.data.preferences.SettingsManager; // importする

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AuthRepository {
    private final SettingsManager settingsManager;

    // Login結果を通知するためのコールバックインターフェース
    public interface LoginCallback {
        void onResult(boolean isSuccess);
    }

    @Inject
    public AuthRepository(SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }

    //パスワード認証の場合のログイン処理
    public void login(String email, String password, LoginCallback callback) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            boolean success = email.equals("test@example.com") && password.equals("password");
            if (success) {
                settingsManager.saveUserName("山田 太郎");
            }
            callback.onResult(success);
        }, 2000);
    }

    // NFC認証の場合のログイン処理
    public void loginWithNfc(String studentId, LoginCallback callback) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            boolean success = studentId != null && !studentId.isEmpty();
            if (success) {
                settingsManager.saveUserName(studentId);
            }
            callback.onResult(success);
        }, 1000);
    }
}