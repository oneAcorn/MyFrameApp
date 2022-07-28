package com.wcy.databasemodule.biz

import com.wcy.databasemodule.dao.ExperimentRecordDao
import com.wcy.databasemodule.entities.ExperimentRecord

/**
 * Created by acorn on 2022/7/28.
 */
class ExperimentRecordBiz : BaseBiz() {
    private val dao = daoSession.experimentRecordDao

    companion object {
        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { ExperimentRecordBiz() }
    }

    @Synchronized
    fun insert(entity: ExperimentRecord?) {
        entity ?: return
        dao.insert(entity)
    }

    fun clearTable() {
        dao.deleteAll()
    }

    fun query(offset: Int, count: Int): List<ExperimentRecord> {
        return dao.queryBuilder().orderAsc(ExperimentRecordDao.Properties.TimeStamp).offset(offset).limit(count)
            .list()
    }
}