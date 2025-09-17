package com.example.lims_android.ui.search.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.lims_android.data.model.AssetResponse;
import com.example.lims_android.data.repository.AssetRepository;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AssetDetailViewModel extends ViewModel {

    private final AssetRepository repository;
    private final MutableLiveData<List<AssetResponse>> _instances = new MutableLiveData<>();
    public LiveData<List<AssetResponse>> instances = _instances;

    @Inject
    public AssetDetailViewModel(AssetRepository repository) {
        this.repository = repository;
    }

    public void loadAssetInstances(long assetMasterId) {
        repository.listAssetsForMaster(assetMasterId, new AssetRepository.RepositoryCallback<List<AssetResponse>>() {
            @Override
            public void onSuccess(List<AssetResponse> data) {
                _instances.postValue(data);
            }
            @Override
            public void onFailure(Exception e) {
                // 必要に応じてエラー処理
            }
        });
    }
}