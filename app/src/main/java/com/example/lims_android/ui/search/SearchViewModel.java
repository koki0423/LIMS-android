package com.example.lims_android.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.lims_android.data.model.Asset;
import com.example.lims_android.data.repository.AssetRepository;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SearchViewModel extends ViewModel {

    private final AssetRepository repository;
    private final MutableLiveData<List<Asset>> _searchResults = new MutableLiveData<>();
    public LiveData<List<Asset>> searchResults = _searchResults;

    @Inject
    public SearchViewModel(AssetRepository repository) {
        this.repository = repository;
    }

    public void search(String query, String category) {
        List<Asset> results = repository.searchAssets(query, category);
        _searchResults.setValue(results);
    }
}