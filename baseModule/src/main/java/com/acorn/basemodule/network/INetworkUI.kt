package com.acorn.basemodule.network

/**
 * Created by acorn on 2019-08-20.
 */
interface INetworkUI {
    /**
     * 显示加载框
     */
    fun showProgressDialog()

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
}