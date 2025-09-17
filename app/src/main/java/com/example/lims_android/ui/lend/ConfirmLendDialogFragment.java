package com.example.lims_android.ui.lend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.lims_android.R;
import com.example.lims_android.data.model.AssetMasterResponse;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ConfirmLendDialogFragment extends DialogFragment {

    private static final String ARG_ASSET = "asset";
    private static final String ARG_BORROWER_ID = "borrower_id";

    private LendViewModel viewModel;

    public static ConfirmLendDialogFragment newInstance(AssetMasterResponse asset, String borrowerId) {
        ConfirmLendDialogFragment fragment = new ConfirmLendDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ASSET, asset);
        args.putString(ARG_BORROWER_ID, borrowerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(LendViewModel.class);
        return inflater.inflate(R.layout.dialog_confirm_lend, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView assetNameText = view.findViewById(R.id.text_view_confirm_asset_name);
        TextView borrowerIdText = view.findViewById(R.id.text_view_confirm_borrower_id);
        Button cancelButton = view.findViewById(R.id.button_cancel);
        Button confirmButton = view.findViewById(R.id.button_confirm_lend);

        AssetMasterResponse asset = (AssetMasterResponse) getArguments().getSerializable(ARG_ASSET);
        String borrowerId = getArguments().getString(ARG_BORROWER_ID);

        assetNameText.setText("資産名: " + asset.getName());
        borrowerIdText.setText("借受者ID: " + borrowerId);

        cancelButton.setOnClickListener(v -> dismiss());

        confirmButton.setOnClickListener(v -> {
            viewModel.executeLend(asset.getManagementNumber(), borrowerId);
        });

        viewModel.lendResultEvent.observe(getViewLifecycleOwner(), result -> {
            if (result.isSuccess) {
                Toast.makeText(getContext(), "貸出が完了しました", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), result.message, Toast.LENGTH_LONG).show();
            }
            dismiss();
        });
    }
}