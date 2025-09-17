package com.example.lims_android.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.lims_android.R;
import com.google.android.material.textfield.TextInputEditText;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SettingsFragment extends Fragment {

    private SettingsViewModel viewModel;
    private TextView userNameTextView;
    private TextInputEditText apiUrlEditText;
    private Button saveButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userNameTextView = view.findViewById(R.id.text_view_user_name);
        apiUrlEditText = view.findViewById(R.id.edit_text_api_url);
        saveButton = view.findViewById(R.id.button_save_settings);

        // 保存ボタンのクリック処理
        saveButton.setOnClickListener(v -> {
            String newUrl = apiUrlEditText.getText().toString();
            viewModel.saveApiUrl(newUrl);
        });

        observeViewModel();

        // ViewModelに設定の読み込みを指示
        viewModel.loadSettings();
    }

    private void observeViewModel() {
        // ユーザー名を監視してUIに反映
        viewModel.userName.observe(getViewLifecycleOwner(), name -> {
            userNameTextView.setText(name);
        });

        // API URLを監視してUIに反映
        viewModel.apiUrl.observe(getViewLifecycleOwner(), url -> {
            apiUrlEditText.setText(url);
        });

        // 保存成功イベントを監視してToastを表示
        viewModel.saveSuccessEvent.observe(getViewLifecycleOwner(), aVoid -> {
            Toast.makeText(getContext(), "設定を保存しました", Toast.LENGTH_SHORT).show();
        });
    }
}