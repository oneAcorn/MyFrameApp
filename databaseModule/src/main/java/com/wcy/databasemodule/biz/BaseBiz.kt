package com.wcy.databasemodule.biz

import com.wcy.databasemodule.DbManager
import com.wcy.databasemodule.dao.DaoSession

/**
 * Created by acorn on 2022/7/20.
 */
abstract class BaseBiz {
    protected val daoSession: DaoSession by lazy { DbManager.instance.getDaoSession() }
}