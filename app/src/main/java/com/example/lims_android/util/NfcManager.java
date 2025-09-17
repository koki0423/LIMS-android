package com.example.lims_android.util;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcF;

import androidx.annotation.NonNull;

import java.io.IOException;

public class NfcManager {

    // 1. NfcManager独自のコールバックインターフェースを定義
    public interface ManagerCallback {
        void onReadSuccess(@NonNull String studentId);
        void onReadFailure(@NonNull Exception exception);
    }

    private final NfcAdapter nfcAdapter;
    private final FeliCaReader feliCaReader;

    public NfcManager(Context context) {
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(context);
        this.feliCaReader = new FeliCaReader();
    }

    public boolean isNfcSupported() {
        return this.nfcAdapter != null;
    }

    public void enableForegroundDispatch(Activity activity) {
        if (!isNfcSupported()) return;
        Intent intent = new Intent(activity, activity.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_MUTABLE);
        IntentFilter[] filters = new IntentFilter[]{new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)};
        String[][] techLists = new String[][]{new String[]{NfcF.class.getName()}};
        nfcAdapter.enableForegroundDispatch(activity, pendingIntent, filters, techLists);
    }

    public void disableForegroundDispatch(Activity activity) {
        if (isNfcSupported()) {
            nfcAdapter.disableForegroundDispatch(activity);
        }
    }

    /**
     * onNewIntentで受け取ったIntentを処理し、NFCタグを読み取ります。
     *
     * @param intent         onNewIntentで受け取ったIntent
     * @param managerCallback 読み取り結果を返す、NfcManager独自のコールバック
     */
    // 2. 引数をNfcManager.ManagerCallbackに変更
    public void handleIntent(@NonNull Intent intent, @NonNull ManagerCallback managerCallback) {
        if (!NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
            return;
        }

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag == null) {
            managerCallback.onReadFailure(new IOException("NFC Tag not found in Intent."));
            return;
        }

        // 3. FeliCaReaderのコールバックを内部で実装し、結果をManagerCallbackに変換して通知する
        feliCaReader.readStudentId(tag, new FeliCaReader.FeliCaCallback() {
            @Override
            public void onSuccess(@NonNull String studentId) {
                // FeliCaReaderからの成功通知をManagerCallback経由で外部に伝える
                managerCallback.onReadSuccess(studentId);
            }

            @Override
            public void onFailure(@NonNull Exception exception) {
                // FeliCaReaderからの失敗通知をManagerCallback経由で外部に伝える
                managerCallback.onReadFailure(exception);
            }
        });
    }
}