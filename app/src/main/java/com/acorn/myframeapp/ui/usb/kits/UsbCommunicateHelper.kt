package com.acorn.myframeapp.ui.usb.kits

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.acorn.basemodule.extendfun.logI

/**
 * Created by acorn on 2023/1/31.
 */

fun AppCompatActivity.registerUsbBroadcast() {

}

class UsbCommunicateHelper(private val logCallback:(String)->Unit){
    private var mReceiver: MyUsbPermissionReceiver? = null

    fun registerBroadcast(activity: AppCompatActivity) {
        if (mReceiver != null)
            return
        activity.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    logI("oncreate")
                    mReceiver = MyUsbPermissionReceiver()
                    val filter = IntentFilter()
                    filter.addAction(UsbConstants.PERMISSION_USB)
                    filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
                    filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
                    activity.registerReceiver(mReceiver, filter)
                }
                Lifecycle.Event.ON_DESTROY -> {
                    mReceiver?.let { activity.unregisterReceiver(it) }
                    mReceiver = null
                }
                else -> {}
            }
        })
    }

    fun test(){
        log("2312dfsfa")
    }

    private fun log(str:String){
        logCallback(str)
    }

    private inner class MyUsbPermissionReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val device =
                intent.getParcelableExtra(UsbManager.EXTRA_DEVICE) as? UsbDevice?
            log("Usb Event ${intent.action},${Thread.currentThread()}")
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