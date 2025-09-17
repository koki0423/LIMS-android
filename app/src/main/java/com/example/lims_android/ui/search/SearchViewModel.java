package com.example.lims_android.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.lims_android.data.model.AssetMasterResponse;
import com.example.lims_android.data.repository.AssetRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SearchViewModel extends ViewModel {

    private final AssetRepository repository;

    // ✅ LiveDataが保持する型を List<Asset> から List<AssetMasterResponse> に変更
    private final MutableLiveData<List<AssetMasterResponse>> _searchResults = new MutableLiveData<>();
    public LiveData<List<AssetMasterResponse>> searchResults = _searchResults;

    @Inject
    public SearchViewModel(AssetRepository repository) {
        this.repository = repository;
    }

    public void search(String query, int genreId) {
        repository.searchAssetMasters(query, genreId, new AssetRepository.RepositoryCallback<List<AssetMasterResponse>>() {
            @Override
            public void onSuccess(List<AssetMasterResponse> data) {
                _searchResults.postValue(data);
            }

            @Override
            public void onFailure(Exception e) {
                // エラー処理（例：空のリストをセットする）
                _searchResults.postValue(new ArrayList<>());
            }
        });
    }
}