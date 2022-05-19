package com.acorn.basemodule.network

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * Created by acorn on 2020/9/2.
 */

/**
 * 一般网络请求的调用链
 */
fun <T> Observable<T>.commonRequest(disposable: CompositeDisposable?): Observable<T> {
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe {
            disposable?.add(it)
        }
}

/**
 * 创建ViewModel实例
 */
fun <T : ViewModel> ViewModelStoreOwner.createViewModel(clazz: Class<T>): T {
    return ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(clazz)
}

/**
 * 创建和Activity共享的ViewModel，当多个Fragment和1个Activity组合，或者一个Activity和1个Fragment想
 * 共同订阅ViewModel的LiveData时，可以用这个方法
 */
fun <T : ViewModel> Fragment.createSharedViewModel(clazz: Class<T>): T? {
    return activity?.createViewModel(clazz)
}