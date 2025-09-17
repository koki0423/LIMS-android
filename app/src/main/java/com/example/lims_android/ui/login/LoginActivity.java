package com.example.lims_android.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.lims_android.ui.main.MainActivity;
import com.example.lims_android.R;
import com.example.lims_android.util.NfcManager;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity implements NfcManager.ManagerCallback {

    private LoginViewModel viewModel;
    private NfcManager nfcManager;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private ProgressBar loadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // ViewModelのインスタンスを取得
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // nfcインスタンスの生成
        nfcManager = new NfcManager(this);

        // UIコンポーネントを初期化
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        loadingProgressBar = findViewById(R.id.loading);

        // ログインボタンのクリックリスナーを設定
        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            viewModel.login(email, password);
        });

        // ViewModelの状態変化を監視（Observe）する
        observeViewModel();
    }


    @Override
    protected void onResume() {
        super.onResume();
        // 画面がフォアグラウンドに来たらNFCの読み取りを有効化
        if (nfcManager.isNfcSupported()) {
            nfcManager.enableForegroundDispatch(this);
        } else {
            Toast.makeText(this, "NFCがサポートされていません", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 画面がバックグラウンドに行ったらNFCの読み取りを無効化
        if (nfcManager.isNfcSupported()) {
            nfcManager.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // NFCカードがタッチされたら、このメソッドが呼ばれる
        // NfcManagerにIntentの処理を依頼する
        nfcManager.handleIntent(intent, this);
    }

    // --- NfcManager.ManagerCallback の実装 ---

    @Override
    public void onReadSuccess(String studentId) {
        // NFCの読み取りが成功したときの処理
        // UIスレッドで実行されるようにする
        runOnUiThread(() -> {
            Toast.makeText(this, "ログイン完了．\nようこそ " + studentId + " さん", Toast.LENGTH_SHORT).show();
            // ViewModelのログイン処理を呼び出す
            viewModel.loginWithStudentId(studentId);
        });
    }

    @Override
    public void onReadFailure(Exception exception) {
        // NFCの読み取りが失敗したときの処理
        runOnUiThread(() -> {
            Toast.makeText(this, "NFCカードの読み取りに失敗しました: " + exception.getMessage(), Toast.LENGTH_LONG).show();
        });
    }

    private void observeViewModel() {
        // ローディング状態を監視
        viewModel.isLoading.observe(this, isLoading -> {
            loadingProgressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            loginButton.setEnabled(!isLoading);
        });

        // ログイン結果を監視
        viewModel.loginResult.observe(this, isSuccess -> {
            if (isSuccess) {
                // 成功した場合: MainActivityに遷移
                Toast.makeText(this, "ログイン成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                // 失敗した場合: エラーメッセージを表示
                Toast.makeText(this, "メールアドレスまたはパスワードが違います", Toast.LENGTH_SHORT).show();
            }
        });
    }
}