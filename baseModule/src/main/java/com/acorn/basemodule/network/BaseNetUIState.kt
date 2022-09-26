package com.acorn.basemodule.network

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.acorn.basemodule.extendfun.appContext

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
            if (it.dialogStateEnum == DialogStateEnum.SHOW) {
                trueNetworkUI.showProgressDialog(null, it.isCancelable)
            } else if (it.dialogStateEnum == DialogStateEnum.DISMISS) {
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
            trueNetworkUI.showTip(it)
        })
        getErrorLayoutState().observe(owner, Observer {
            it ?: return@Observer
            trueNetworkUI.showErrorLayout()
        })
    }

    override fun showProgressDialog(msg: String?, cancelable: Boolean?) {
        dialogState.value = DialogState(DialogStateEnum.SHOW, cancelable ?: true)
    }

    override fun showProgressDialog(msgRes: Int, vararg params: Any?, cancelable: Boolean?) {
        dialogState.value = DialogState(DialogStateEnum.SHOW, cancelable ?: true)
    }

    override fun dismissProgressDialog() {
        dialogState.value = DialogState(DialogStateEnum.DISMISS, true)
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

    override fun showTip(string: String) {
        errorToastState.value = string
    }

    override fun showTip(msgRes: Int, vararg params: Any?) {
        val msg = if (params.isEmpty()) {
            appContext.getString(msgRes)
        } else {
            appContext.getString(msgRes, params)
        }
        errorToastState.value = msg
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

    data class DialogState(val dialogStateEnum: DialogStateEnum, val isCancelable: Boolean)

    enum class DialogStateEnum {
        SHOW,
        DISMISS
    }

    enum class SuccessState {
        CONTENT,
        NULL
    }
}