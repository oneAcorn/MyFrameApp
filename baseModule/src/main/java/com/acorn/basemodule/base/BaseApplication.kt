package com.acorn.basemodule.base

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.tencent.smtt.sdk.QbSdk

/**
 * Created by acorn on 2022/5/19.
 */
open class BaseApplication : MultiDexApplication() {

    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        initTencentX5()
    }

    private fun initTencentX5() {
        QbSdk.initX5Environment(applicationContext, object : QbSdk.PreInitCallback {
            override fun onCoreInitFinished() {
                // 内核初始化完成，可能为系统内核，也可能为系统内核(复制过来的,文档就这么写的..)
            }

            /**
             * 预初始化结束
             * 由于X5内核体积较大，需要依赖网络动态下发，所以当内核不存在的时候，默认会回调false，此时将会使用系统内核代替
             * @param isX5 是否使用X5内核
             */
            override fun onViewInitFinished(p0: Boolean) {
            }

        })
        //（可选）为了提高内核占比，在初始化前可配置允许移动网络下载内核（大小 40-50 MB）。默认移动网络不下载
        QbSdk.setDownloadWithoutWifi(true);
    }
}