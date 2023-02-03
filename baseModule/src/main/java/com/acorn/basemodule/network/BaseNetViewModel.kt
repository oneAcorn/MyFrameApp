package com.acorn.basemodule.network

import androidx.lifecycle.ViewModel
import com.acorn.basemodule.network.BaseNetUIState
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.lang.ref.WeakReference

/**
 * Created by acorn on 2020/6/8.
 */
open class BaseNetViewModel : ViewModel() {
    protected val disposable = CompositeDisposable()

    private var mUiRef: WeakReference<INetworkUI>? = null

    override fun onCleared() {
        super.onCleared()
        if (!disposable.isDisposed)
            disposable.dispose()
    }

    fun attachUI(ui: INetworkUI) {
        mUiRef = WeakReference(ui)
    }

    fun detachUI() {
        mUiRef?.clear()
        mUiRef = null
    }

    protected fun getUI(): INetworkUI? = mUiRef?.get()

    open fun isActivityShared() = false
}