package com.acorn.myframeapp.ui.usb.service

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.Service
import android.content.*
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.acorn.basemodule.extendfun.appContext
import com.acorn.basemodule.extendfun.logI
import com.acorn.basemodule.network.MyException
import com.acorn.basemodule.network.commonRequest
import com.acorn.myframeapp.bean.UsbBean
import com.acorn.myframeapp.bean.UsbResponseState
import com.acorn.myframeapp.bean.isConnected
import com.acorn.myframeapp.ui.usb.kits.ByteUtils
import com.acorn.myframeapp.ui.usb.kits.UsbConstants
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable

/**
 *
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
    private val listenerMap = mutableMapOf<String, ILogListener>()
    val mUsbDevices = mutableListOf<UsbBean>()
    private var usbDevicesListener: IUsbDevicesState? = null
    private val disposable: CompositeDisposable = CompositeDisposable()
    private val mHandler = Handler(Looper.getMainLooper())

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
        if (!disposable.isDisposed)
            disposable.dispose()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder {
        registerBroadcast()
        initUsbDevices()
        return UsbCommunicateBinder()
    }

    private fun initUsbDevices() {
        usbManager = appContext.getSystemService(Context.USB_SERVICE) as UsbManager
        val deviceList = usbManager.deviceList
        val deviceIterator: Iterator<UsbDevice> = deviceList.values.iterator()
        while (deviceIterator.hasNext()) {
            val device = deviceIterator.next()
//            logI("init:${device.productName}")
            if (device.isWcyDevice()) {
                mUsbDevices.add(UsbBean(device))
            }
        }
//        showToast("listener:${usbDeviceListener},${mUsbDevices.size}")
        usbDevicesListener?.usbDevicesInit(mUsbDevices)
    }

    private fun postLog(str: String) {
        mHandler.post {
            log(str)
        }
    }

    private fun log(str: String) {
        listenerMap.values.forEach {
            it.log(str)
        }
        logI(str)
    }

    //region Usb相关方法

    @SuppressLint("CheckResult")
    fun sendMsg(usbBean: UsbBean?) {
        usbBean ?: return
        Observable.create<UsbResponseState> { emitter ->
            val conn = usbBean.usbConnection
            val endpointOut = usbBean.endpointOut
            if (conn == null || endpointOut == null || !usbBean.isConnected()) {
                emitter.onError(MyException("usb doesn't connect"))
            } else {
                //参考https://juejin.cn/post/6918663138892054536
                //requestType根据定义,HID设备能用的只有10100001(input(device->host),二进制,转成16进制为0xA1)
                //和00100001(output(host->device),二进制,转成16进制为0x21)两种
                //request此处使用的是0x09:SET_REPORT
                val cmd = "5aa53b0007038e00000000981fb75bb5".toByteArrayBigOrder()
//                val ret = conn.controlTransfer(0x21, 0x09, 0x2110, 0, cmd, cmd.size, 3000)
//                val ret = conn.controlTransfer(0x80, 0x06, 0x1021, 0, cmd, cmd.size, 3000)
                val ret = conn.bulkTransfer(endpointOut, cmd, cmd.size, 3000)
                postLog("sendMsg.:$ret")
                if (ret > 0) {
                    emitter.onNext(UsbResponseState.Success(cmd.copyOfRange(0, ret)))
                    emitter.onComplete()
                } else {
                    emitter.onError(MyException("read null"))
                }
            }
        }.commonRequest(disposable)
            .subscribe({ state ->
                if (state is UsbResponseState.Success) {
                    log("recv:${state.byteArray?.toLogString()}")
                } else if (state is UsbResponseState.Error) {
                    log("recv error:${state.e?.message}")
                }
            }, {
                log(it.message ?: "error")
                it.printStackTrace()
            })
    }

    @SuppressLint("CheckResult")
    fun recvMsgBulk(usbBean: UsbBean?) {
        usbBean ?: return
        Observable.create<UsbResponseState> { emitter ->
            val endPointIn = usbBean.endpointIn
            val conn = usbBean.usbConnection
            if (endPointIn == null || conn == null || !usbBean.isConnected()) {
                emitter.onError(MyException("usb doesn't connect"))
            } else {
                val maxSize = endPointIn.maxPacketSize
                val byteArr = ByteArray(maxSize)
                val ret = conn.bulkTransfer(endPointIn, byteArr, maxSize, 3000)
                postLog("recv0 maxSize:${maxSize},ret:${ret}")
                if (ret > 0) {
                    emitter.onNext(UsbResponseState.Success(byteArr.copyOfRange(0, ret)))
                    emitter.onComplete()
                } else {
                    emitter.onError(MyException("read null"))
                }
            }
        }
            .commonRequest(disposable)
            .subscribe({
                if (it is UsbResponseState.Success) {
                    log("recv:${it.byteArray?.toLogString()}")
                } else if (it is UsbResponseState.Error) {
                    log("recv error:${it.e?.message}")
                }
            }, {
                log(it.message ?: "error")
                it.printStackTrace()
            })
    }

    private fun String.toByteArrayBigOrder(): ByteArray = ByteUtils.toByteArrayBigOrder(this)

    /**
     * @param division 分隔符
     */
    private fun ByteArray.toLogString(division: String? = ","): String {
        val sb = StringBuilder()
        for (b in this) {
            sb.append(String.format("%02x", b))
            division?.let { sb.append(division) }
        }
        return sb.toString()
    }

    fun requestUsbPermission(usbBean: UsbBean) {
        usbBean.requestPermission()
    }

    /**
     * 判断是否是公司自己的设备
     */
    private fun UsbDevice.isWcyDevice(): Boolean {
        return productId == UsbConstants.PRODUCT_ID && vendorId == UsbConstants.VENDOR_ID
    }

    private fun UsbBean.hasPermission(): Boolean = usbManager.hasPermission(this.device)

    private fun UsbBean.requestPermission() {
        val pendingIntent =
            PendingIntent.getBroadcast(appContext, 0, Intent(UsbConstants.PERMISSION_USB), 0)
        usbManager.requestPermission(this.device, pendingIntent)
    }

    private fun UsbBean.connectUsb() {
        if (!hasPermission()) {
            requestPermission()
            return
        }
        val mUsbInterface = device.getInterface(0)
        log("usb interface:${device.interfaceCount},${mUsbInterface.endpointCount}")
        val connection = usbManager.openDevice(device)
        if (connection?.claimInterface(mUsbInterface, true) == true) {
//            showToast("conn interface:${usbBean.device.interfaceCount},${mUsbInterface.endpointCount}")
            //获取接口上的两个端点，分别对应 OUT 和 IN
            for (i in 0 until mUsbInterface.endpointCount) {
                val end = mUsbInterface.getEndpoint(i)
                val dir = end.direction
                val type = end.type
                if (endpointIn == null &&
                    dir == android.hardware.usb.UsbConstants.USB_DIR_IN &&
                    type == android.hardware.usb.UsbConstants.USB_ENDPOINT_XFER_INT
                ) {
                    endpointIn = end
                    log("endpointIn")
                } else if (endpointOut == null &&
                    dir == android.hardware.usb.UsbConstants.USB_DIR_OUT &&
                    type == android.hardware.usb.UsbConstants.USB_ENDPOINT_XFER_INT
                ) {
                    endpointOut = end
                    log("endpointOut")
                }
            }
            usbInterface = mUsbInterface
            usbConnection = connection
//            getDeviceInfo(usbBean)
//            getDeviceInfoMainThread(usbBean)
//            listenerList.forEach {
//                it.onDataSetChanged(mUsbDevices)
//            }
            usbDevicesListener?.usbConnected(this)
        } else {
            log("failed")
            connection?.close()
        }
    }
    //endregion

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
            log("Usb Event ${intent.action},${device?.deviceName},${device?.productName}")
            when (intent.action) {
                UsbConstants.PERMISSION_USB -> { //申请USB权限的回调
                    synchronized(this) {
//                        context.unregisterReceiver(this)
                        val isPermissionGranted =
                            intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)
                        if (isPermissionGranted) {
                            //授权成功后，进行USB设备操作
                            existUsbBean?.connectUsb()
                        } else {
                            // 拒绝
                        }
                    }
                }
                UsbManager.ACTION_USB_DEVICE_ATTACHED -> { //新USB设备插入
                    var newUsbBean: UsbBean? = null
                    if (existUsbBean == null && device != null && device.isWcyDevice()) {
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
        fun addListener(activity: AppCompatActivity, listener: ILogListener) {
            //同一个activity，不同实例也需要区分开
            listenerMap[activity.toString()] = listener
        }

        fun removeListener(activity: AppCompatActivity) {
            listenerMap.remove(activity.toString())
        }

        fun setUsbDevicesStateListner(listener: IUsbDevicesState) {
            usbDevicesListener = listener
            mUsbDevices.takeIf { it.isNotEmpty() }?.let { usbDevicesListener?.usbDevicesInit(it) }
        }

        fun getService(): UsbCommunicateService {
            return this@UsbCommunicateService
        }
    }
}