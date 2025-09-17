package com.example.lims_android.di;

import com.example.lims_android.data.repository.AuthRepository;
import com.example.lims_android.data.repository.UserRepository;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

import javax.inject.Singleton;

// Hiltに依存性の注入方法を教えるモジュール
@Module
// このモジュールがアプリケーションシングルトンのライフサイクルを持つことを指定
@InstallIn(SingletonComponent.class)
public class RepositoryModule {

    @Provides
    @Singleton
    public UserRepository provideUserRepository() {
        return new UserRepository();
    }
}