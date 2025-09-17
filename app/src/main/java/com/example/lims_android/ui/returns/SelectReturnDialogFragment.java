package com.example.lims_android.ui.returns;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lims_android.R;
import com.example.lims_android.ui.returns.adapter.LendAdapter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SelectReturnDialogFragment extends DialogFragment {

    private static final String ARG_MANAGEMENT_NUMBER = "management_number";
    private SelectReturnViewModel viewModel;
    private LendAdapter adapter;
    private RecyclerView recyclerView;
    private TextView noLendsText;

    public static SelectReturnDialogFragment newInstance(String managementNumber) {
        SelectReturnDialogFragment fragment = new SelectReturnDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MANAGEMENT_NUMBER, managementNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ダイアログとそのウィンドウがnullでないことを確認
        if (getDialog() != null && getDialog().getWindow() != null) {
            // 画面の横幅の90%をダイアログの幅に設定
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.95);
            // 高さはコンテンツに合わせる
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            // ウィンドウにサイズを適用
            getDialog().getWindow().setLayout(width, height);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(SelectReturnViewModel.class);
        return inflater.inflate(R.layout.dialog_select_return, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler_view_lends);
        noLendsText = view.findViewById(R.id.text_view_no_lends);

        setupRecyclerView();
        observeViewModel();

        if (getArguments() != null) {
            String managementNumber = getArguments().getString(ARG_MANAGEMENT_NUMBER);
            viewModel.fetchLends(managementNumber);
        }
    }

    private void setupRecyclerView() {
        adapter = new LendAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(lend -> {
            // リストの項目がクリックされたら返却処理を実行
            viewModel.executeReturn(lend.getLendUlid());
        });
    }

    private void observeViewModel() {
        viewModel.lendsList.observe(getViewLifecycleOwner(), lends -> {
            if (lends == null || lends.isEmpty()) {
                noLendsText.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                noLendsText.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                adapter.submitList(lends);
            }
        });

        viewModel.returnResult.observe(getViewLifecycleOwner(), isSuccess -> {
            if (isSuccess) {
                Toast.makeText(getContext(), "返却が完了しました", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "返却に失敗しました", Toast.LENGTH_SHORT).show();
            }
            dismiss();
        });
    }
}