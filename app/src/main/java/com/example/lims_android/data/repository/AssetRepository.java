package com.example.lims_android.data.repository;

import com.example.lims_android.data.model.Asset;
import com.example.lims_android.data.model.AssetMasterResponse;
import com.example.lims_android.data.model.ErrorBody;
import com.example.lims_android.data.model.LendResponse;
import com.example.lims_android.data.model.LendsApiResponse;
import com.example.lims_android.data.network.ApiService;
import com.example.lims_android.data.network.CreateLendRequest;
import com.example.lims_android.data.network.CreateReturnRequest;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.ResponseBody;

@Singleton
public class AssetRepository {
    private final List<Asset> allAssets = new ArrayList<>();

    private final ApiService apiService; // ✅ ApiServiceを注入

    // 結果を通知するためのコールバック
    public interface RepositoryCallback<T> {
        void onSuccess(T data);

        void onFailure(Exception e);
    }

    @Inject
    public AssetRepository(ApiService apiService) { // ✅ コンストラクタで受け取る
        this.apiService = apiService;
    }

    public void fetchAssetMaster(String managementNumber, RepositoryCallback<AssetMasterResponse> callback) {
        apiService.getAssetMaster(managementNumber).enqueue(new retrofit2.Callback<AssetMasterResponse>() {
            @Override
            public void onResponse(retrofit2.Call<AssetMasterResponse> call, retrofit2.Response<AssetMasterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Exception("Error: " + response.code()));
                }
            }

            @Override
            public void onFailure(retrofit2.Call<AssetMasterResponse> call, Throwable t) {
                callback.onFailure(new Exception(t));
            }
        });
    }

    public List<Asset> searchAssets(String query, String category) {
        // Stream APIを使ってリストをフィルタリング
        return allAssets.stream()
                .filter(asset -> {
                    boolean categoryMatch = "すべて".equals(category) || asset.getDescription().equals(category);
                    boolean queryMatch = query.isEmpty() ||
                            asset.getName().toLowerCase().contains(query.toLowerCase()) ||
                            asset.getBarcode().contains(query);
                    return categoryMatch && queryMatch;
                })
                .collect(Collectors.toList());
    }
    public void lendAsset(String managementNumber, String borrowerId, RepositoryCallback<Void> callback) {
        CreateLendRequest request = new CreateLendRequest(1, borrowerId);

        apiService.lendAsset(managementNumber, request).enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(retrofit2.Call<Void> call, retrofit2.Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null);
                } else {
                    // ✅ レスポンスが成功でない場合 (4xx, 5xxエラー)
                    String errorMessage = "不明なエラーが発生しました。";
                    ResponseBody errorBody = response.errorBody();
                    if (errorBody != null) {
                        try {
                            // JSONをErrorBodyクラスに変換
                            Gson gson = new Gson();
                            ErrorBody errorResponse = gson.fromJson(errorBody.string(), ErrorBody.class);
                            if (errorResponse != null && errorResponse.getError() != null) {
                                // "message"フィールドの値を取得
                                errorMessage = errorResponse.getError().getMessage();
                            }
                        } catch (IOException e) {
                            errorMessage = "エラー応答の解析に失敗しました。";
                        }
                    }
                    callback.onFailure(new Exception(errorMessage)); // 解析したメッセージを渡す
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                // 通信自体が失敗した場合
                callback.onFailure(new Exception(t));
            }
        });
    }

    public void listLends(String managementNumber, RepositoryCallback<List<LendResponse>> callback) {
        apiService.listLends(managementNumber,false).enqueue(new retrofit2.Callback<LendsApiResponse>() {
            @Override
            public void onResponse(retrofit2.Call<LendsApiResponse> call, retrofit2.Response<LendsApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getItems());
                } else {
                    callback.onFailure(new Exception("Error: " + response.code()));
                }
            }
            @Override
            public void onFailure(retrofit2.Call<LendsApiResponse> call, Throwable t) {
                callback.onFailure(new Exception(t));
            }
        });
    }

    public void returnAsset(String lendUlid, String returneeId, RepositoryCallback<Void> callback) {
        CreateReturnRequest request = new CreateReturnRequest(1, returneeId); // 数量1で固定
        apiService.returnAsset(lendUlid, request).enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(retrofit2.Call<Void> call, retrofit2.Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null);
                } else {
                    callback.onFailure(new Exception("Error: " + response.code()));
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                callback.onFailure(new Exception(t));
            }
        });
    }
}