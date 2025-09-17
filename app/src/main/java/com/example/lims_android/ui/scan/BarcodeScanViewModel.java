package com.example.lims_android.ui.scan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.lims_android.util.SingleLiveEvent;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class BarcodeScanViewModel extends ViewModel {
    private final SingleLiveEvent<String> _scannedBarcode = new SingleLiveEvent<>();
    public LiveData<String> scannedBarcode = _scannedBarcode;

    @Inject
    public BarcodeScanViewModel() {
    }

    public void onBarcodeScanned(String barcode) {
        // スキャンが一時停止中か、または既に結果を処理中の場合は何もしない
        if (_scannedBarcode.getValue() != null) return;
        // 資産情報を取得
        if (barcode != null && !barcode.isEmpty()) {
            _scannedBarcode.setValue(barcode);
        }
    }

    //スキャン結果をリセットする
    public void onScanResultHandled() {
        _scannedBarcode.setValue(null);
    }
}