package com.acorn.myframeapp.ui.recyclerview.largedata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import java.util.concurrent.Flow

/**
 * Created by acorn on 2022/7/27.
 */
class PagingViewModel : ViewModel() {
    val dataFlow = Pager(
        PagingConfig(
            pageSize = 10,
            //预刷新的距离，距离最后一个 item 多远时加载数据，默认为 pageSize
            prefetchDistance = 5,
            initialLoadSize = 20,
            //一次应在内存中保存的最大数据，默认为 Int.MAX_VALUE
            maxSize = 100,
            enablePlaceholders = false
        ),
        initialKey = 0
    ) {
        LargeDataSource()
    }
        .flow.cachedIn(viewModelScope) //cachedIn：绑定协程生命周期，必须加上，否则可能崩溃；
}