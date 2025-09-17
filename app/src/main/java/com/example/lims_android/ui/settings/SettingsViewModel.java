package com.example.lims_android.ui.settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.lims_android.data.preferences.SettingsManager;
import com.example.lims_android.util.SingleLiveEvent;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SettingsViewModel extends ViewModel {

    private final SettingsManager settingsManager;

    private final MutableLiveData<String> _userName = new MutableLiveData<>();
    public LiveData<String> userName = _userName;

    private final MutableLiveData<String> _apiUrl = new MutableLiveData<>();
    public LiveData<String> apiUrl = _apiUrl;

    private final SingleLiveEvent<Void> _saveSuccessEvent = new SingleLiveEvent<>();
    public LiveData<Void> saveSuccessEvent = _saveSuccessEvent;

    @Inject
    public SettingsViewModel(SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }

    // 画面が表示されたときに設定を読み込む
    public void loadSettings() {
        _userName.setValue(settingsManager.getUserName());
        _apiUrl.setValue(settingsManager.getApiUrl());
    }

    // API URLを保存する
    public void saveApiUrl(String newUrl) {
        settingsManager.saveApiUrl(newUrl);
        _saveSuccessEvent.setValue(null); // 保存成功イベントを通知
    }
}