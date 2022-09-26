package com.acorn.basemodule.network

import androidx.annotation.StringRes

/**
 * Created by acorn on 2019-08-20.
 */
interface INetworkUI {
    /**
     * 显示加载框
     */
    fun showProgressDialog(msg: String? = null, cancelable: Boolean? = true)

    fun showProgressDialog(@StringRes msgRes: Int, vararg params: Any?, cancelable: Boolean? = true)

    /**
     * 关闭加载框
     */
    fun dismissProgressDialog()

    /**
     * 错误页面
     */
    fun showErrorLayout()

    /**
     * 正常显示的页面
     */
    fun showContentLayout()

    /**
     * 数据为空的页面
     */
    fun showNullLayout()

    /**
     * 显示提示
     */
    fun showTip(string: String)

    fun showTip(@StringRes msgRes: Int, vararg params: Any?)
}