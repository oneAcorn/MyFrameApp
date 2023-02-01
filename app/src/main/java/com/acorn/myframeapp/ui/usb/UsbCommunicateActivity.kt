package com.acorn.myframeapp.ui.usb

import android.os.Bundle
import com.acorn.myframeapp.R
import com.acorn.myframeapp.demo.BaseDemoActivity
import com.acorn.myframeapp.demo.Demo

/**
 * Created by acorn on 2023/2/1.
 */
class UsbCommunicateActivity : BaseDemoActivity() {
    override fun getItems(): Array<Demo> {
        TODO("Not yet implemented")
    }

    override fun onItemClick(data: Demo, idOrPosition: Int) {
        TODO("Not yet implemented")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usb_communicate)
    }

    override fun initView() {
        super.initView()
        showToolbar("Usb Communication")
    }
}