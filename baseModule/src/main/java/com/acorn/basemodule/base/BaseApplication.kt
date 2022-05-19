package com.acorn.basemodule.base

import android.content.Context
import androidx.multidex.MultiDexApplication

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
    }
}