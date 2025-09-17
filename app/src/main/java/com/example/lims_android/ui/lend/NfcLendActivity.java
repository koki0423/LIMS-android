package com.example.lims_android.ui.lend;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lims_android.R;
import com.example.lims_android.util.NfcManager;
import com.google.android.material.textfield.TextInputEditText;

public class NfcLendActivity extends AppCompatActivity implements NfcManager.ManagerCallback {

    public static final String EXTRA_BORROWER_ID = "borrowerId";
    public static final String EXTRA_MANAGEMENT_NUMBER = "managementNumber"; // ✅ 追加

    private NfcManager nfcManager;
    private TextInputEditText borrowerIdEditText; // ✅ 追加
    private Button confirmButton; // ✅ 追加

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_lend);
        nfcManager = new NfcManager(this);

        // --- UIの初期化 ---
        TextView managementNumberTextView = findViewById(R.id.text_view_management_number);
        borrowerIdEditText = findViewById(R.id.edit_text_borrower_id);
        confirmButton = findViewById(R.id.button_confirm_manual_input);

        // --- 受け取った管理番号を表示 ---
        String managementNumber = getIntent().getStringExtra(EXTRA_MANAGEMENT_NUMBER);
        managementNumberTextView.setText(managementNumber);

        // --- 手動確定ボタンの処理 ---
        confirmButton.setOnClickListener(v -> {
            String borrowerId = borrowerIdEditText.getText().toString();
            if (borrowerId.isEmpty()) {
                Toast.makeText(this, "借受者IDを入力してください", Toast.LENGTH_SHORT).show();
            } else {
                returnBorrowerId(borrowerId);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcManager.isNfcSupported()) {
            nfcManager.enableForegroundDispatch(this);
        } else {
            Toast.makeText(this, "NFCがサポートされていません", Toast.LENGTH_SHORT).show();
            finish(); // NFCが使えなければActivityを閉じる
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcManager.isNfcSupported()) {
            nfcManager.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        nfcManager.handleIntent(intent, this);
    }


    @Override
    public void onReadSuccess(String studentId) {
        // NFC成功時も共通の返却処理を呼び出す
        runOnUiThread(() -> {
            returnBorrowerId(studentId);
        });
    }

    @Override
    public void onReadFailure(Exception exception) {
        runOnUiThread(() -> {
            Toast.makeText(this, "NFC読み取り失敗", Toast.LENGTH_SHORT).show();
        });
    }

    private void returnBorrowerId(String borrowerId) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_BORROWER_ID, borrowerId);
        setResult(RESULT_OK, resultIntent);

        Toast.makeText(this, "読み取り完了！", Toast.LENGTH_SHORT).show();
        // 例：TextViewのテキストを変更する
        // successTextView.setVisibility(View.VISIBLE);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            finish(); // 0.5秒後にこのActivityを閉じる
        }, 500);
    }
}