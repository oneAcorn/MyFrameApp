package com.acorn.myframeapp.ui.recyclerview.largedata2

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.acorn.basemodule.extendfun.logI
import com.acorn.myframeapp.ui.recyclerview.largedata.LargeDataBean
import com.wcy.databasemodule.entities.ExperimentRecord

/**
 * Created by acorn on 2022/7/28.
 */
@OptIn(ExperimentalPagingApi::class)
class MyRemoteMediator : RemoteMediator<Int, ExperimentRecord>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ExperimentRecord>
    ): MediatorResult {
        try {
            // 第一步： 判断 LoadType
            val pageKey = when (loadType) {
                // 首次访问 或者调用 PagingDataAdapter.refresh()
                LoadType.REFRESH -> null

                // 在当前加载的数据集的开头加载数据时
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)

                // 上拉加载更多时触发
                LoadType.APPEND -> {
                    val lastItem= state.lastItemOrNull()
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = true
                        )
                    lastItem.pageNo
                }
            }
            logI("loadType:$loadType")
            return MediatorResult.Success(endOfPaginationReached = !DataGenerator.isGenerating)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
}