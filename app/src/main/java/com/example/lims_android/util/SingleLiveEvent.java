package com.example.lims_android.util;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 一度だけ通知されるイベントを扱うためのLiveData。
 * 画面回転などで再購読された際に、古いイベントが再通知されるのを防ぎます。
 *
 * @param <T> イベントのデータ型
 */
public class SingleLiveEvent<T> extends MutableLiveData<T> {

    private final AtomicBoolean mPending = new AtomicBoolean(false);

    @MainThread
    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull final Observer<? super T> observer) {
        // 通常のLiveDataをラップし、イベントが処理済みかどうかをチェックする
        super.observe(owner, t -> {
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(t);
            }
        });
    }

    @MainThread
    @Override
    public void setValue(T t) {
        mPending.set(true);
        super.setValue(t);
    }
}