package com.acorn.myframeapp.bean

/**
 * Created by acorn on 2023/2/3.
 */
sealed class UsbBeanUIState {
    data class Init(val list: List<UsbBean>?) : UsbBeanUIState()

    data class Insert(val bean: UsbBean) : UsbBeanUIState()

    data class Remove(val bean: UsbBean) : UsbBeanUIState()
}