package com.acorn.myframeapp.ui.usb

import android.content.res.Configuration
import android.os.Bundle
import com.acorn.basemodule.extendfun.logI
import com.acorn.basemodule.extendfun.singleClick
import com.acorn.basemodule.network.createViewModel
import com.acorn.myframeapp.R
import com.acorn.myframeapp.bean.UsbBean
import com.acorn.myframeapp.bean.isConnected
import com.acorn.myframeapp.demo.BaseDemoActivity
import com.acorn.myframeapp.demo.Demo
import com.acorn.myframeapp.ui.usb.dialog.UsbConnectDialog
import com.acorn.myframeapp.ui.usb.service.IUsbListener
import com.acorn.myframeapp.ui.usb.service.UsbCommunicateService
import com.acorn.myframeapp.ui.usb.service.bindUsbCommunicateService
import com.acorn.myframeapp.ui.usb.viewmodel.UsbViewModel
import kotlinx.android.synthetic.main.activity_usb_communicate.*

/**
 * Created by acorn on 2023/2/1.
 */
class UsbCommunicateActivity : BaseDemoActivity<UsbViewModel>(), IUsbListener {
    private var usbBinder: UsbCommunicateService.UsbCommunicateBinder? = null

    companion object {
        private const val CLICK_CONNECT_DIALOG = 0
        private const val SEND_MSG = 1
        private const val RECV_MSG_CTRL = 2
        private const val RECV_MSG_BULK = 3
    }

    override fun getItems(): Array<Demo> {
        return arrayOf(
            Demo("ConnectDialog", CLICK_CONNECT_DIALOG),
            Demo("Send msg", SEND_MSG),
            Demo("Receive msg controlTransfer", RECV_MSG_CTRL),
            Demo("Receive msg bulkTransfer", RECV_MSG_BULK)
        )
    }

    override fun onItemClick(data: Demo, idOrPosition: Int) {
        when (idOrPosition) {
            CLICK_CONNECT_DIALOG -> {
                UsbConnectDialog.newInstance { usbBean ->
                    usbBinder?.getService()?.requestUsbPermission(usbBean)
                }.show(supportFragmentManager, "ConnectDialog")
            }
            SEND_MSG -> {
                usbBinder?.getService()?.sendMsg(getTestDevice())
            }
            RECV_MSG_CTRL -> {
//                usbBinder?.getService()?.recvMsgControl(getTestDevice())
            }
            RECV_MSG_BULK -> {
                usbBinder?.getService()?.recvMsgBulk(getTestDevice())
            }
        }
    }

    private fun getTestDevice(): UsbBean? {
        val bean = usbBinder?.getService()?.mUsbDevices?.takeIf { it.isNotEmpty() }?.get(0)
            ?.takeIf { it.isConnected() }
        if (bean == null) {
            showTip("没有设备")
        }
        return bean
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        log(newConfig.toString())
    }

    override fun initView() {
        super.initView()
        setContentView(R.layout.activity_usb_communicate)
        showToolbar("Usb Communication")
    }

    override fun initListener() {
        super.initListener()
        clearBtn.singleClick {
            logTv.text = null
        }
    }

    override fun initData() {
        super.initData()
        bindUsbCommunicateService { binder ->
            binder.addListener(this, this)
            usbBinder = binder
//            it.setUsbDevicesStateListner()
            mViewModel?.let { binder.setUsbDevicesStateListner(it) }
        }
    }

    override fun getViewModel(): UsbViewModel? = createViewModel(UsbViewModel::class.java)

    override fun log(str: String) {
        logTv.text = "${logTv.text.toString()?.takeIf { it.isNotEmpty() } ?: ""}\n$str"
        logI(str)
//        showTip(str)
    }
}