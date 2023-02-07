package com.acorn.myframeapp.bean

import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbEndpoint
import android.hardware.usb.UsbInterface
import android.os.Parcel
import android.os.Parcelable
import com.acorn.myframeapp.ui.usb.kits.ByteUtils
import kotlin.concurrent.thread

/**
 * Created by acorn on 2023/2/3.
 */
data class UsbBean(
    val device: UsbDevice,
    var usbInterface: UsbInterface? = null,
    var endpointIn: UsbEndpoint? = null, //输入(相对于Host(上位机)的输入)
    var endpointOut: UsbEndpoint? = null, //输出(相对于Host(上位机)的输出)
    var usbConnection: UsbDeviceConnection? = null
)

fun UsbBean.isConnected(): Boolean =
    endpointIn != null && endpointOut != null && usbConnection != null