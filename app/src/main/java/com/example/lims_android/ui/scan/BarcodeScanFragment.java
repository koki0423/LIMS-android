package com.example.lims_android.ui.scan;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton; // ImageButtonをimport
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.Camera; // Cameraをimport
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.lims_android.R;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class BarcodeScanFragment extends Fragment {

    private static final String TAG = "BarcodeScanFragment";
    private volatile boolean isScanningActive = true;
    private BarcodeScanViewModel viewModel;
    private PreviewView previewView;
    private ExecutorService cameraExecutor;

    // --- UIとカメラ制御用の変数を追加 ---
    private ImageButton flashButton;
    private Camera camera;

    // カメラ権限リクエスト用のランチャー
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    startCamera();
                } else {
                    Toast.makeText(requireContext(), "カメラの権限が拒否されました", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(BarcodeScanViewModel.class);
        View root = inflater.inflate(R.layout.fragment_barcode_scan, container, false); // レイアウトファイル名を確認してください

        // IDを変更
        previewView = root.findViewById(R.id.preview_view_camera);
        // フラッシュボタンを初期化
        flashButton = root.findViewById(R.id.button_flash_light);

        cameraExecutor = Executors.newSingleThreadExecutor();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // カメラ権限のチェック
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }

        // ViewModelの監視
        viewModel.scannedBarcode.observe(getViewLifecycleOwner(), barcodeValue -> {
            if (barcodeValue != null) {
                // ✅ スキャンを一時停止
                isScanningActive = false;

                ScanResultDialogFragment dialog = ScanResultDialogFragment.newInstance(barcodeValue);
                dialog.show(getParentFragmentManager(), "ScanResultDialog");

                viewModel.onScanResultHandled();
            }
        });

        // ✅ DialogFragmentからの結果（閉じた通知）を待つリスナー
        getParentFragmentManager().setFragmentResultListener(ScanResultDialogFragment.REQUEST_KEY, this, (requestKey, result) -> {
            // ✅ ダイアログが閉じたので、スキャンを再開する
            isScanningActive = true;
        });
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext());

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());
                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();
                imageAnalysis.setAnalyzer(cameraExecutor, this::scanBarcodes); // メソッド参照に
                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

                cameraProvider.unbindAll();

                // カメラをライフサイクルにバインドし、Cameraオブジェクトを取得
                this.camera = cameraProvider.bindToLifecycle(
                        getViewLifecycleOwner(), cameraSelector, preview, imageAnalysis);

                // フラッシュライトのセットアップ
                setupFlashlight();

            } catch (Exception e) {
                Log.e(TAG, "CameraXのバインドに失敗", e);
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }

    // フラッシュライトのセットアップと制御メソッド
    private void setupFlashlight() {
        if (camera != null && camera.getCameraInfo().hasFlashUnit()) {
            flashButton.setVisibility(View.VISIBLE);

            // フラッシュの状態を監視してアイコンを更新
            camera.getCameraInfo().getTorchState().observe(getViewLifecycleOwner(), torchState -> {
                if (torchState == androidx.camera.core.TorchState.ON) {
                    flashButton.setImageResource(R.drawable.ic_flash_off);
                } else {
                    flashButton.setImageResource(R.drawable.ic_flash_on);
                }
            });

            // ボタンクリックでフラッシュをトグル
            flashButton.setOnClickListener(v -> {
                camera.getCameraControl().enableTorch(
                        camera.getCameraInfo().getTorchState().getValue() != androidx.camera.core.TorchState.ON
                );
            });
        } else {
            flashButton.setVisibility(View.GONE);
        }
    }

    private void scanBarcodes(androidx.camera.core.ImageProxy imageProxy) {
        if (!isScanningActive) {
            imageProxy.close();
            return;
        }

        @SuppressWarnings("UnsafeOptInUsageError")
        android.media.Image mediaImage = imageProxy.getImage();
        if (mediaImage != null) {
            InputImage image = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
            BarcodeScanner scanner = BarcodeScanning.getClient();

            scanner.process(image)
                    .addOnSuccessListener(barcodes -> {
                        if (!barcodes.isEmpty()) {
                            String rawValue = barcodes.get(0).getRawValue();
                            viewModel.onBarcodeScanned(rawValue);
                        }
                    })
                    .addOnFailureListener(e -> Log.e(TAG, "バーコードのスキャンに失敗", e))
                    .addOnCompleteListener(task -> imageProxy.close());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cameraExecutor.shutdown();
    }
}