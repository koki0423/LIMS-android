package com.example.lims_android.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.lims_android.data.repository.AuthRepository;

import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LoginViewModel extends ViewModel {

    private final AuthRepository authRepository;

    // UIの状態を管理するためのLiveData
    // ログイン処理中かどうか
    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    public LiveData<Boolean> isLoading = _isLoading;

    // ログイン結果（成功: true, 失敗: false）
    private final MutableLiveData<Boolean> _loginResult = new MutableLiveData<>();
    public LiveData<Boolean> loginResult = _loginResult;

    @Inject
    public LoginViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void login(String email, String password) {
        _isLoading.setValue(true); // ローディング開始
        authRepository.login(email, password, isSuccess -> {
            _loginResult.postValue(isSuccess); // 結果を通知
            _isLoading.postValue(false);       // ローディング終了
        });
    }
    public void loginWithStudentId(String studentId) {
        _isLoading.setValue(true); // ローディング開始
        authRepository.loginWithNfc(studentId, isSuccess -> {
            _loginResult.postValue(isSuccess); // 結果を通知
            _isLoading.postValue(false);       // ローディング終了
        });
    }
}