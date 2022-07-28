package com.acorn.myframeapp.ui.recyclerview.largedata2

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.acorn.basemodule.extendfun.logI
import com.acorn.myframeapp.ui.recyclerview.largedata.LargeDataBean
import com.wcy.databasemodule.biz.ExperimentRecordBiz
import com.wcy.databasemodule.entities.ExperimentRecord


/**
 * Created by acorn on 2022/7/27.
 */
/**
 * 第一个泛型:分页标识类型，如页码，则为Int
 */
class LargeDataSourceDynamic : PagingSource<Int, ExperimentRecord>() {
    private var nextKey: Int? = null

    /**
     * 假设这里需要做一些后台线程的数据加载任务。
     *
     * @param startPosition
     * @param count
     * @return
     */
    private fun loadData(params: LoadParams<Int>): List<ExperimentRecord> {
        val pageNo = params.key ?: 0
        val list = ExperimentRecordBiz.instance.query(pageNo * params.loadSize, params.loadSize)
        for (bean in list) {
            bean.pageNo = pageNo
        }
        nextKey = if (list.size < params.loadSize) {
            null
        } else {
            pageNo + 1
        }
        return list
    }

    override fun getRefreshKey(state: PagingState<Int, ExperimentRecord>): Int? =
        null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ExperimentRecord> {
        return try {
            val pageNo = params.key ?: 0
            val prevKey = if (pageNo > 0) {
                pageNo - 1
            } else {
                null
            }
            logI("dynamic pageNo:${params.key}")
            LoadResult.Page(
                data = loadData(params),
                prevKey = prevKey, //如果可以往上加载更多就设置该参数，否则不设置
                nextKey = nextKey //加载下一页的key 如果传null就说明到底了
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }


}
