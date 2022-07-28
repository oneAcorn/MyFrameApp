package com.acorn.myframeapp.ui.recyclerview.largedata2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.wcy.databasemodule.entities.ExperimentRecord

/**
 * Created by acorn on 2022/7/28.
 */
@OptIn(ExperimentalPagingApi::class)
class DynamicPagingViewModel : ViewModel() {
    val dataFlow = Pager(
        PagingConfig(
            pageSize = 10,
            //预刷新的距离，距离最后一个 item 多远时加载数据，默认为 pageSize
            prefetchDistance = 1,
            initialLoadSize = 20,
            //一次应在内存中保存的最大数据，默认为 Int.MAX_VALUE
            maxSize = 100,
            enablePlaceholders = false
        ),
        remoteMediator = MyRemoteMediator(),
        initialKey = 0,
    ) {
        LargeDataSourceDynamic()
    }
        .flow.cachedIn(viewModelScope) //cachedIn：绑定协程生命周期，必须加上，否则可能崩溃；
}