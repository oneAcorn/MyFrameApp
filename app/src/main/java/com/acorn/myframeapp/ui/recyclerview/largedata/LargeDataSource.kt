package com.acorn.myframeapp.ui.recyclerview.largedata

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.acorn.basemodule.extendfun.logI


/**
 * Created by acorn on 2022/7/27.
 */
/**
 * 第一个泛型:分页标识类型，如页码，则为Int
 */
class LargeDataSource : PagingSource<LargeDataKey, LargeDataBean>() {

    /**
     * 假设这里需要做一些后台线程的数据加载任务。
     *
     * @param startPosition
     * @param count
     * @return
     */
    private fun loadData(startPosition: Int, count: Int): List<LargeDataBean> {
        val list = mutableListOf<LargeDataBean>()
        for (i in 0 until count) {
            val id = startPosition + i
            val data = LargeDataBean(id, "content:$id")
            list.add(data)
        }
        return list
    }

    override fun getRefreshKey(state: PagingState<LargeDataKey, LargeDataBean>): LargeDataKey? =
        null

    override suspend fun load(params: LoadParams<LargeDataKey>): LoadResult<LargeDataKey, LargeDataBean> {
        return try {
            val pageNo = params.key?.pageNo ?: 0
            val prevKey = if (pageNo > 0) {
                LargeDataKey(pageNo - 1, "what")
            } else {
                null
            }
            logI("pageNo:${params.key}")
            LoadResult.Page(
                data = loadData(pageNo * params.loadSize, params.loadSize),
                prevKey = prevKey, //如果可以往上加载更多就设置该参数，否则不设置
                nextKey = if (pageNo == 5) null else LargeDataKey(
                    pageNo + 1,
                    "this?"
                ) //加载下一页的key 如果传null就说明到底了
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }


}

data class LargeDataKey(val pageNo: Int, val somethingElse: String)