package com.example.lims_android.ui.lend;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.lims_android.data.repository.AssetRepository;
import com.example.lims_android.util.SingleLiveEvent;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
import com.example.lims_android.data.model.LendResult;

@HiltViewModel
public class LendViewModel extends ViewModel {

    private final AssetRepository repository;
    private final SingleLiveEvent<LendResult> _lendResultEvent = new SingleLiveEvent<>();
    public LiveData<LendResult> lendResultEvent = _lendResultEvent;

    @Inject
    public LendViewModel(AssetRepository repository) {
        this.repository = repository;
    }

    public void executeLend(String managementNumber, String borrowerId) {
        repository.lendAsset(managementNumber, borrowerId, new AssetRepository.RepositoryCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                _lendResultEvent.postValue(new LendResult(true, null)); // 成功
            }

            @Override
            public void onFailure(Exception e) {
                _lendResultEvent.postValue(new LendResult(false, "error: "+e.getMessage()));
            }
        });
    }
}