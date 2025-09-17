package com.example.lims_android.ui.returns; // returnsパッケージを作成

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.lims_android.data.preferences.SettingsManager;
import com.example.lims_android.data.repository.AssetRepository;
import com.example.lims_android.util.SingleLiveEvent;

import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ReturnViewModel extends ViewModel {

    private final AssetRepository repository;
    private final SettingsManager settingsManager;
    private final SingleLiveEvent<Boolean> _returnResult = new SingleLiveEvent<>();
    public LiveData<Boolean> returnResult = _returnResult;

    @Inject
    public ReturnViewModel(AssetRepository repository, SettingsManager settingsManager) {
        this.repository = repository;
        this.settingsManager = settingsManager;
    }

    public void executeReturn(String managementNumber) {
        // 現在ログインしているユーザーを返却者として設定
        String returneeId = settingsManager.getUserName();
        repository.returnAsset(managementNumber, returneeId, new AssetRepository.RepositoryCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                _returnResult.postValue(true);
            }

            @Override
            public void onFailure(Exception e) {
                _returnResult.postValue(false);
            }
        });
    }
}