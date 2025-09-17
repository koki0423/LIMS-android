package com.example.lims_android.data.repository;

import com.example.lims_android.data.model.User;
import javax.inject.Inject;

// リポジトリの具体的な実装クラス
public class UserRepository {

    @Inject
    public UserRepository() {
        // APIクライアントやDAOなどを初期化する場合はここで行う
    }

    public User getAuthenticatedUser() {
        // 本来はAPIやDBからユーザー情報を取得する
        return new User("Taro Yamada", "taro.yamada@example.com");
    }
}