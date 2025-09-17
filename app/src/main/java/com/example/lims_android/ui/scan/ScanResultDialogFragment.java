package com.example.lims_android.ui.scan;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.lims_android.R;
import com.example.lims_android.data.model.AssetMasterResponse;
import com.example.lims_android.ui.lend.LendViewModel;
import com.example.lims_android.ui.lend.NfcLendActivity;
import com.example.lims_android.ui.returns.ReturnViewModel;
import com.example.lims_android.data.model.LendResult;
import com.example.lims_android.ui.returns.SelectReturnDialogFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ScanResultDialogFragment extends DialogFragment {
    public static final String REQUEST_KEY = "scanResultDialogRequest";
    private static final String ARG_MANAGEMENT_NUMBER = "management_number";
    private ScanResultViewModel scanViewModel;
    private LendViewModel lendViewModel;
    private ReturnViewModel returnViewModel;

    private final ActivityResultLauncher<Intent> nfcLendLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    String borrowerId = result.getData().getStringExtra(NfcLendActivity.EXTRA_BORROWER_ID);
                    AssetMasterResponse asset = scanViewModel.asset.getValue();

                    if (borrowerId != null && asset != null) {
                        lendViewModel.executeLend(asset.getManagementNumber(), borrowerId);
                    }
                }
            });

    public static ScanResultDialogFragment newInstance(String managementNumber) {
        ScanResultDialogFragment fragment = new ScanResultDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MANAGEMENT_NUMBER, managementNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.95);
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            getDialog().getWindow().setLayout(width, height);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        scanViewModel = new ViewModelProvider(this).get(ScanResultViewModel.class);
        lendViewModel = new ViewModelProvider(this).get(LendViewModel.class);
        returnViewModel = new ViewModelProvider(this).get(ReturnViewModel.class);
        return inflater.inflate(R.layout.dialog_scan_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ProgressBar progressBar = view.findViewById(R.id.progress_bar);
        TextView nameTextView = view.findViewById(R.id.text_view_asset_name);
        TextView numberTextView = view.findViewById(R.id.text_view_management_number);
        LinearLayout buttonLayout = view.findViewById(R.id.layout_buttons);
        Button closeButton = view.findViewById(R.id.button_close);
        Button lendButton = view.findViewById(R.id.button_lend);
        Button returnButton = view.findViewById(R.id.button_return);

        // --- LiveDataの監視 ---
        scanViewModel.isLoading.observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        scanViewModel.asset.observe(getViewLifecycleOwner(), asset -> {
            nameTextView.setVisibility(View.VISIBLE);
            numberTextView.setVisibility(View.VISIBLE);
            buttonLayout.setVisibility(View.VISIBLE);
            nameTextView.setText(asset.getName());
            numberTextView.setText("管理番号: " + asset.getManagementNumber());
        });

        scanViewModel.error.observe(getViewLifecycleOwner(), error -> {
            Toast.makeText(getContext(), "エラー: " + error, Toast.LENGTH_LONG).show();
            dismiss();
        });

        lendViewModel.lendResultEvent.observe(getViewLifecycleOwner(), result -> {
            if (result.isSuccess) {
                Toast.makeText(getContext(), "貸出処理が完了しました", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), result.message, Toast.LENGTH_LONG).show();
            }
            dismiss();
        });

        // --- ボタンのクリックリスナーを設定 ---
        lendButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), NfcLendActivity.class);
            AssetMasterResponse asset = scanViewModel.asset.getValue();
            if (asset != null) {
                intent.putExtra(NfcLendActivity.EXTRA_MANAGEMENT_NUMBER, asset.getManagementNumber());
                nfcLendLauncher.launch(intent);
            }
        });

        returnButton.setOnClickListener(v -> {
            AssetMasterResponse asset = scanViewModel.asset.getValue();
            if (asset != null) {
                SelectReturnDialogFragment dialog = SelectReturnDialogFragment.newInstance(asset.getManagementNumber());
                dialog.show(getParentFragmentManager(), "SelectReturnDialog");
                dismiss();
            }
        });

        closeButton.setOnClickListener(v -> {
            dismiss(); // ダイアログを閉じる
        });

        // --- 初期データ取得 ---
        if (getArguments() != null) {
            String managementNumber = getArguments().getString(ARG_MANAGEMENT_NUMBER);
            scanViewModel.loadAssetDetails(managementNumber);
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        // BarcodeScanFragmentでスキャンを再開させるための通知
        getParentFragmentManager().setFragmentResult(REQUEST_KEY, new Bundle());
    }
}