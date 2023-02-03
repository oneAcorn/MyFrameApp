package com.acorn.myframeapp.ui.usb

import android.content.res.Configuration
import android.os.Bundle
import com.acorn.basemodule.extendfun.logI
import com.acorn.basemodule.network.createViewModel
import com.acorn.myframeapp.R
import com.acorn.myframeapp.demo.BaseDemoActivity
import com.acorn.myframeapp.demo.Demo
import com.acorn.myframeapp.ui.usb.dialog.UsbConnectDialog
import com.acorn.myframeapp.ui.usb.service.IUsbListener
import com.acorn.myframeapp.ui.usb.service.bindUsbCommunicateService
import com.acorn.myframeapp.ui.usb.viewmodel.UsbViewModel
import kotlinx.android.synthetic.main.activity_usb_communicate.*

/**
 * Created by acorn on 2023/2/1.
 */
class UsbCommunicateActivity : BaseDemoActivity<UsbViewModel>(), IUsbListener {
    companion object {
        private const val CLICK_CONNECT_DIALOG = 0
    }

    override fun getItems(): Array<Demo> {
        return arrayOf(
            Demo("ConnectDialog", CLICK_CONNECT_DIALOG)
        )
    }

    override fun onItemClick(data: Demo, idOrPosition: Int) {
        when (idOrPosition) {
            CLICK_CONNECT_DIALOG -> {
                UsbConnectDialog.newInstance().show(supportFragmentManager, "ConnectDialog")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usb_communicate)
//        showTip("onCreate")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        log(newConfig.toString())
    }

    override fun initView() {
        super.initView()
        showToolbar("Usb Communication")
    }

    override fun initData() {
        super.initData()
        bindUsbCommunicateService { binder ->
            binder.addListener(this, this)
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