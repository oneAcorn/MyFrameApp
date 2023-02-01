package com.acorn.myframeapp.ui.usb.kits

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by acorn on 2023/1/31.
 */

fun AppCompatActivity.registerUsbBroadcast() {

}

class UsbCommunicateHelper private constructor() {
    companion object {
        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { UsbCommunicateHelper() }
    }

    fun registerBroadcast() {

    }

    private inner class MyUsbPermissionReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val device =
                intent.getParcelableExtra(UsbManager.EXTRA_DEVICE) as? UsbDevice?
            when (intent.action) {
                UsbConstants.PERMISSION_USB -> { //申请USB权限的回调
                }
                UsbManager.ACTION_USB_DEVICE_ATTACHED -> { //新USB设备插入
                }
                UsbManager.ACTION_USB_DEVICE_DETACHED -> { //新USB设备拔出
                }
            }
        }
    }
}