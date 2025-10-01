package com.example.lims_android.di;

import android.util.Log;

import com.example.lims_android.data.network.ApiService;
import com.example.lims_android.data.preferences.SettingsManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {

    private static final String TAG = "NET";

    @Provides
    @Singleton
    public HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor interceptor =
                new HttpLoggingInterceptor(message -> Log.d("HTTP", message));
        // 本番で INFO/NONE に落とすのを推奨
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient(HttpLoggingInterceptor logging) {
        return new OkHttpClient.Builder()
                .addInterceptor(logging)              // リクエスト/レスポンスBODYまで出す
                .eventListenerFactory(call -> new NetEvents())
                // .connectTimeout(10, TimeUnit.SECONDS)  // 必要なら
                // .readTimeout(20, TimeUnit.SECONDS)
                .build();
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(SettingsManager settingsManager,
                                    OkHttpClient okHttpClient) {
        String rawBaseUrl = settingsManager.getApiUrl(); // 例: https://192.168.0.10:8443/
        if (rawBaseUrl == null) rawBaseUrl = "";

        // RetrofitのbaseUrlは必ず末尾が / 必須
        if (!rawBaseUrl.endsWith("/")) {
            rawBaseUrl = rawBaseUrl + "/";
        }

        // URLバリデーション（不正ならログ出してフォールバック）
        HttpUrl httpUrl = HttpUrl.parse(rawBaseUrl);
        if (httpUrl == null) {
            Log.e(TAG, "Invalid baseUrl in Settings: " + rawBaseUrl + " -> fallback to https://127.0.0.1:8443/");
            httpUrl = HttpUrl.parse("https://127.0.0.1:8443/");
        } else {
            Log.i(TAG, "Retrofit baseUrl = " + httpUrl);
        }

        return new Retrofit.Builder()
                .baseUrl(httpUrl)
                .client(okHttpClient)                 // ★ ここが重要
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public ApiService provideApiService(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }

}
