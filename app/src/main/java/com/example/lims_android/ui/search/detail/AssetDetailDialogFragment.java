package com.example.lims_android.ui.search.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lims_android.R;
import com.example.lims_android.ui.search.adapter.AssetInstanceAdapter;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AssetDetailDialogFragment extends DialogFragment {

    private static final String ARG_ASSET_MASTER_ID = "asset_master_id";
    private AssetDetailViewModel viewModel;
    private AssetInstanceAdapter adapter;

    public static AssetDetailDialogFragment newInstance(long assetMasterId) {
        AssetDetailDialogFragment fragment = new AssetDetailDialogFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ASSET_MASTER_ID, assetMasterId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(AssetDetailViewModel.class);
        return inflater.inflate(R.layout.dialog_asset_detail, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.95);
            getDialog().getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_asset_instances);
        Button closeButton = view.findViewById(R.id.button_close_detail);
        adapter = new AssetInstanceAdapter();
        recyclerView.setAdapter(adapter);

        closeButton.setOnClickListener(v -> dismiss());

        viewModel.instances.observe(getViewLifecycleOwner(), instances -> {
            adapter.submitList(instances);
        });

        if (getArguments() != null) {
            long masterId = getArguments().getLong(ARG_ASSET_MASTER_ID);
            viewModel.loadAssetInstances(masterId);
        }
    }
}