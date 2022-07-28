package com.wcy.databasemodule

import com.acorn.basemodule.extendfun.appContext
import com.wcy.databasemodule.dao.DaoMaster
import com.wcy.databasemodule.dao.DaoSession

/**
 * Created by acorn on 2022/7/20.
 */
class DbManager {
    private lateinit var daoSession: DaoSession

    companion object {
        private const val DB_NAME = "wcy.db"
        val instance: DbManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { DbManager() }
    }

    fun initDatabase() {
        val db = DevOpenHelper(appContext, DB_NAME).writableDb
        daoSession = DaoMaster(db).newSession()
    }

    fun getDaoSession(): DaoSession = daoSession
}