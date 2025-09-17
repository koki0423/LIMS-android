package com.example.lims_android.data.network;

import com.example.lims_android.data.model.AssetMasterResponse;
import com.example.lims_android.data.model.AssetMastersApiResponse;
import com.example.lims_android.data.model.AssetsResponse;
import com.example.lims_android.data.model.LendsApiResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    /**
     * 資産マスター情報を取得する
     * @param managementNumber URLのパスに埋め込む管理番号
     */
    @GET("assets/masters/{management_number}")
    Call<AssetMasterResponse> getAssetMaster(
            @Path("management_number") String managementNumber
    );

    /**
     * 貸出一覧を取得する
     * @param managementNumber クエリパラメータとして付与する管理番号
     * @param returned クエリパラメータとして付与する返却済みフラグ
     */
    @GET("lends")
    Call<LendsApiResponse> listLends(
            @Query("management_number") String managementNumber,
            @Query("returned") boolean returned
    );

    /**
     * 資産を貸し出す
     * @param managementNumber URLのパスに埋め込む管理番号
     * @param lendRequest リクエストボディ
     */
    @POST("assets/{management_number}/lends")
    Call<Void> lendAsset(
            @Path("management_number") String managementNumber,
            @Body CreateLendRequest lendRequest
    );

    /**
     * 資産を返却する
     * @param lendUlid URLのパスに埋め込む貸出ID
     * @param returnRequest リクエストボディ
     */
    @POST("lends/{lend_ulid}/returns")
    Call<Void> returnAsset(
            @Path("lend_ulid") String lendUlid,
            @Body CreateReturnRequest returnRequest
    );

    @GET("assets/masters")
    Call<AssetMastersApiResponse> listAssetMasters(
            @Query("name") String nameQuery,
            @Query("genre") int genre
    );

    @GET("assets")
    Call<AssetsResponse> listAssets(@Query("asmi") long assetMasterId);
}