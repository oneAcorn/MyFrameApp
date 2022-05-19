package com.acorn.basemodule.network

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * 基础网络UI状态，包括显示加载框，失败的布局，toast，空布局等
 * Created by acorn on 2020/6/10.
 */
class BaseNetUIState : INetworkUI {
    private val dialogState = MutableLiveData<DialogState>()
    private val successState = MutableLiveData<SuccessState>()

    //因为有可能同时显示errorToast和errorLayout，所以把失败状态分开为2个，防止LiveData事件丢失问题
    private val errorToastState = MutableLiveData<String>()
    private val errorLayoutState = MutableLiveData<Boolean>()

    fun observe(owner: LifecycleOwner, trueNetworkUI: INetworkUI) {
        getDialogState().observe(owner, Observer {
            it ?: return@Observer
            if (it == DialogState.SHOW) {
                trueNetworkUI.showProgressDialog()
            } else if (it == DialogState.DISMISS) {
                trueNetworkUI.dismissProgressDialog()
            }
        })
        getSuccessState().observe(owner, Observer {
            it ?: return@Observer
            if (it == SuccessState.CONTENT) {
                trueNetworkUI.showContentLayout()
            } else if (it == SuccessState.NULL) {
                trueNetworkUI.showNullLayout()
            }
        })
        getErrorToastState().observe(owner, Observer {
            it ?: return@Observer
            trueNetworkUI.showToast(it)
        })
        getErrorLayoutState().observe(owner, Observer {
            it ?: return@Observer
            trueNetworkUI.showErrorLayout()
        })
    }

    override fun showProgressDialog() {
        dialogState.value = DialogState.SHOW
    }

    override fun dismissProgressDialog() {
        dialogState.value = DialogState.DISMISS
    }

    override fun showContentLayout() {
        successState.value = SuccessState.CONTENT
    }

    override fun showNullLayout() {
        successState.value = SuccessState.NULL
    }

    override fun showErrorLayout() {
        errorLayoutState.value = true
    }

    override fun showToast(string: String) {
        errorToastState.value = string
    }

    fun getDialogState(): LiveData<DialogState> {
        return dialogState
    }

    fun getSuccessState(): LiveData<SuccessState> {
        return successState
    }

    fun getErrorToastState(): LiveData<String> {
        return errorToastState
    }

    fun getErrorLayoutState(): LiveData<Boolean> {
        return errorLayoutState
    }

    enum class DialogState {
        SHOW,
        DISMISS
    }

    enum class SuccessState {
        CONTENT,
        NULL
    }
}