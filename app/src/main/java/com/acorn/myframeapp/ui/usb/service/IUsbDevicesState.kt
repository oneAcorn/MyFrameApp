package com.acorn.myframeapp.ui.usb.service

import com.acorn.myframeapp.bean.UsbBean

/**
 * Created by acorn on 2023/2/3.
 */
interface IUsbDevicesState {
    fun usbDevicesInit(list: List<UsbBean>)

    fun usbDeviceInsert(bean: UsbBean)

    fun usbDeviceRemove(bean: UsbBean)

    fun usbConnected(bean: UsbBean)
}