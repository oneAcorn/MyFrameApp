package com.acorn.myframeapp

import com.acorn.basemodule.base.BaseApplication
import com.wcy.databasemodule.DbManager

/**
 * Created by acorn on 2022/5/19.
 */
class MyApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        initDatabase()
    }

    private fun initDatabase() {
        DbManager.instance.initDatabase()
    }
}