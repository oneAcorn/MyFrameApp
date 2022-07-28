package com.wcy.databasemodule

import android.content.Context
import com.wcy.databasemodule.dao.DaoMaster
import org.greenrobot.greendao.database.Database

/**
 * Created by acorn on 2022/7/20.
 */
class DevOpenHelper(context: Context, name: String) : DaoMaster.OpenHelper(context,name) {

    //升级数据库
    override fun onUpgrade(db: Database, oldVersion: Int, newVersion: Int) {
        super.onUpgrade(db, oldVersion, newVersion)
        //有需求时可使用com.github.yuweiguocn:GreenDaoUpgradeHelper
    }
}