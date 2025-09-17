package com.example.lims_android.ui.scan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.lims_android.data.model.AssetMasterResponse;
import com.example.lims_android.data.repository.AssetRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ScanResultViewModel extends ViewModel {

    private final AssetRepository repository;

    private final MutableLiveData<AssetMasterResponse> _asset = new MutableLiveData<>();
    public LiveData<AssetMasterResponse> asset = _asset;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(true);
    public LiveData<Boolean> isLoading = _isLoading;

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public LiveData<String> error = _error;

    @Inject
    public ScanResultViewModel(AssetRepository repository) {
        this.repository = repository;
    }

    public void loadAssetDetails(String managementNumber) {
        _isLoading.setValue(true);
        repository.fetchAssetMaster(managementNumber, new AssetRepository.RepositoryCallback<AssetMasterResponse>() {
            @Override
            public void onSuccess(AssetMasterResponse data) {
                _asset.postValue(data);
                _isLoading.postValue(false);
            }

            @Override
            public void onFailure(Exception e) {
                _error.postValue(e.getMessage());
                _isLoading.postValue(false);
            }
        });
    }
}