package com.acorn.basemodule.network

import androidx.lifecycle.ViewModel
import com.acorn.basemodule.network.BaseNetUIState
import io.reactivex.rxjava3.disposables.CompositeDisposable

/**
 * Created by acorn on 2020/6/8.
 */
open class BaseNetViewModel : ViewModel() {
    protected val disposable = CompositeDisposable()

    /**
     * 通用的网络监听
     * 注：当有多个网络请求，并可能引起冲突时，建议单独创建一个BaseNetUIState
     */
    val commonState: BaseNetUIState by lazy { BaseNetUIState() }

    override fun onCleared() {
        super.onCleared()
        if (!disposable.isDisposed)
            disposable.dispose()
    }
}