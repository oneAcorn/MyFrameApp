package com.acorn.myframeapp.ui.usb.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.acorn.basemodule.network.BaseNetViewModel
import com.acorn.myframeapp.bean.UsbBean
import com.acorn.myframeapp.bean.UsbBeanUIState
import com.acorn.myframeapp.ui.usb.service.IUsbDevicesState

/**
 * Created by acorn on 2023/2/3.
 */
class UsbViewModel : BaseNetViewModel(), IUsbDevicesState {
    private val _usbDevicesLiveData by lazy { MutableLiveData<UsbBeanUIState>() }
    val usbDevicesLiveData: LiveData<UsbBeanUIState> = _usbDevicesLiveData

    override fun isActivityShared(): Boolean {
        return true
    }

    override fun usbDevicesInit(list: List<UsbBean>) {
        _usbDevicesLiveData.value = UsbBeanUIState.Init(list)
    }

    override fun usbDeviceInsert(bean: UsbBean) {
        _usbDevicesLiveData.value = UsbBeanUIState.Insert(bean)
    }

    override fun usbDeviceRemove(bean: UsbBean) {
        _usbDevicesLiveData.value = UsbBeanUIState.Remove(bean)
    }
}