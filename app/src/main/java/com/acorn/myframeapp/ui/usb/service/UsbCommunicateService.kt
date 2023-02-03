package com.acorn.myframeapp.ui.usb.service

import android.app.Service
import android.content.*
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Binder
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.acorn.basemodule.extendfun.appContext
import com.acorn.myframeapp.bean.UsbBean
import com.acorn.myframeapp.ui.usb.kits.UsbConstants

/**
 * Created by acorn on 2023/1/31.
 */

fun AppCompatActivity.bindUsbCommunicateService(callback: ((UsbCommunicateService.UsbCommunicateBinder) -> Unit)) {
    var binder: UsbCommunicateService.UsbCommunicateBinder? = null

    val serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            if (service !is UsbCommunicateService.UsbCommunicateBinder) return
            binder = service
            callback(service)
        }

    }

    lifecycle.addObserver(LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> {

            }
            Lifecycle.Event.ON_DESTROY -> {
                binder?.removeListener(this)
                unbindService(serviceConnection)
            }
            else -> {
            }
        }
    })

    val serviceIntent = Intent(this, UsbCommunicateService::class.java)
    bindService(serviceIntent, serviceConnection, AppCompatActivity.BIND_AUTO_CREATE)
}

class UsbCommunicateService : Service() {
    private lateinit var usbManager: UsbManager
    private var mReceiver: MyUsbPermissionReceiver? = null
    private val listenerMap = mutableMapOf<String, IUsbListener>()
    val mUsbDevices = mutableListOf<UsbBean>()
    private var usbDevicesListener: IUsbDevicesState? = null

    private fun registerBroadcast() {
        if (mReceiver != null) return
        mReceiver = MyUsbPermissionReceiver()
        val filter = IntentFilter()
        filter.addAction(UsbConstants.PERMISSION_USB)
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
        registerReceiver(mReceiver, filter)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        mReceiver?.let { unregisterReceiver(it) }
        mReceiver = null
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder {
        registerBroadcast()
        initUsbDevices()
        return UsbCommunicateBinder()
    }

    private fun initUsbDevices(){
        usbManager = appContext.getSystemService(Context.USB_SERVICE) as UsbManager
        val deviceList = usbManager.deviceList
        val deviceIterator: Iterator<UsbDevice> = deviceList.values.iterator()
        while (deviceIterator.hasNext()) {
            val device = deviceIterator.next()
//            logI("init:${device.productName}")
            if (verifyDevice(device)) {
                mUsbDevices.add(UsbBean(device))
            }
        }
//        showToast("listener:${usbDeviceListener},${mUsbDevices.size}")
        usbDevicesListener?.usbDevicesInit(mUsbDevices)
    }

    private fun log(str: String) {
        listenerMap.values.forEach {
            it.log(str)
        }
    }

    /**
     * 判断是否是公司自己的设备
     */
    private fun verifyDevice(usbDevice: UsbDevice?): Boolean {
        return usbDevice?.productId == UsbConstants.PRODUCT_ID && usbDevice.vendorId == UsbConstants.VENDOR_ID
    }

    private inner class MyUsbPermissionReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val device =
                intent.getParcelableExtra(UsbManager.EXTRA_DEVICE) as? UsbDevice?
            var existUsbBean: UsbBean? = null
            for (bean in mUsbDevices) {
                //UsbDevice.equal()判断的是device.deviceName
                //deviceName即使是同一个设备,每次插入都会不同.
                if (bean.device == device) {
                    existUsbBean = bean
                    break
                }
            }
            log("Usb Event ${intent.action},${Thread.currentThread()}")
            when (intent.action) {
                UsbConstants.PERMISSION_USB -> { //申请USB权限的回调
                }
                UsbManager.ACTION_USB_DEVICE_ATTACHED -> { //新USB设备插入
                    var newUsbBean: UsbBean? = null
                    if (existUsbBean == null && device != null && verifyDevice(device)) {
                        newUsbBean = UsbBean(device)
                    }
//                    showToast("新设备插入:${device?.deviceName},$usbBean")
                    newUsbBean?.let {
                        mUsbDevices.add(it)
                        usbDevicesListener?.usbDeviceInsert(newUsbBean)
                    }
                }
                UsbManager.ACTION_USB_DEVICE_DETACHED -> { //新USB设备拔出
                    existUsbBean?.let {
                        mUsbDevices.remove(it)
                        usbDevicesListener?.usbDeviceRemove(it)
                    }
                }
            }
        }
    }

    inner class UsbCommunicateBinder : Binder() {
        fun addListener(activity: AppCompatActivity, listener: IUsbListener) {
            //同一个activity，不同实例也需要区分开
            listenerMap[activity.toString()] = listener
        }

        fun removeListener(activity: AppCompatActivity) {
            listenerMap.remove(activity.toString())
        }

        fun setUsbDevicesStateListner(listener: IUsbDevicesState) {
            usbDevicesListener = listener
        }

        fun getService(): UsbCommunicateService {
            return this@UsbCommunicateService
        }
    }
}