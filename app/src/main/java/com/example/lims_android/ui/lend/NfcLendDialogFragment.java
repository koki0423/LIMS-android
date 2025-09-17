package com.example.lims_android.ui.lend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.lims_android.R;
import com.example.lims_android.util.NfcManager;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NfcLendDialogFragment extends DialogFragment implements NfcManager.ManagerCallback {

    public static final String REQUEST_KEY = "nfcLendRequest";
    public static final String KEY_BORROWER_ID = "borrowerId";

    private NfcManager nfcManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        nfcManager = new NfcManager(requireContext());
        return inflater.inflate(R.layout.activity_nfc_lend, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (nfcManager.isNfcSupported()) {
            // ✅ 引数にはActivityのインスタンスを渡す
            nfcManager.enableForegroundDispatch(requireActivity());
        } else {
            Toast.makeText(getContext(), "NFCがサポートされていません", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (nfcManager.isNfcSupported()) {
            // ✅ 引数にはActivityのインスタンスを渡す
            nfcManager.disableForegroundDispatch(requireActivity());
        }
    }

    /**
     * ✅ MainActivityからNFCインテントを受け取るための公開メソッド
     */
    public void processNfcIntent(Intent intent) {
        // ★ステップ4：FragmentまでIntentが到達したか？
        Log.d("NFC_DEBUG", "--- NfcLendDialogFragment processNfcIntent CALLED! ---");
        nfcManager.handleIntent(intent, this);
    }

    // --- NfcManager.ManagerCallback の実装 ---
    @Override
    public void onReadSuccess(String studentId) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                // 読み取ったIDを結果としてセットし、ダイアログを閉じる
                Bundle result = new Bundle();
                result.putString(KEY_BORROWER_ID, studentId);
                getParentFragmentManager().setFragmentResult(REQUEST_KEY, result);
                dismiss();
            });
        }
    }

    @Override
    public void onReadFailure(Exception exception) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                Toast.makeText(getContext(), "NFCカードの読み取りに失敗しました", Toast.LENGTH_SHORT).show();
            });
        }
    }
}