package com.example.lims_android.ui.returns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.lims_android.data.model.LendResponse;
import com.example.lims_android.data.preferences.SettingsManager;
import com.example.lims_android.data.repository.AssetRepository;
import com.example.lims_android.util.SingleLiveEvent;

import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SelectReturnViewModel extends ViewModel {

    private final AssetRepository repository;
    private final SettingsManager settingsManager;

    private final MutableLiveData<List<LendResponse>> _lendsList = new MutableLiveData<>();
    public LiveData<List<LendResponse>> lendsList = _lendsList;

    private final SingleLiveEvent<Boolean> _returnResult = new SingleLiveEvent<>();
    public LiveData<Boolean> returnResult = _returnResult;

    // isLoading, errorなどのLiveDataも同様に定義...

    @Inject
    public SelectReturnViewModel(AssetRepository repository, SettingsManager settingsManager) {
        this.repository = repository;
        this.settingsManager = settingsManager;
    }

    public void fetchLends(String managementNumber) {
        repository.listLends(managementNumber, new AssetRepository.RepositoryCallback<List<LendResponse>>() {
            @Override
            public void onSuccess(List<LendResponse> data) {
                _lendsList.postValue(data);
            }
            @Override
            public void onFailure(Exception e) {
                // エラー処理...
            }
        });
    }

    public void executeReturn(String lendUlid) {
        String returneeId = settingsManager.getUserName();
        repository.returnAsset(lendUlid, returneeId, new AssetRepository.RepositoryCallback<Void>() {
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