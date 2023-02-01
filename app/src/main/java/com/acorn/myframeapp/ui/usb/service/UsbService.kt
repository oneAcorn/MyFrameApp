package com.acorn.myframeapp.ui.usb.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

/**
 * Created by acorn on 2023/1/31.
 */
class UsbService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder {
        return UsbBinder()
    }

    inner class UsbBinder : Binder() {

        fun getService(): UsbService {
            return this@UsbService
        }
    }
}