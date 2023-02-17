package com.acorn.myframeapp.ui.coroutines.flow.viewmodels

import com.acorn.myframeapp.ui.coroutines.flow.bean.News

/**
 * Created by acorn on 2023/1/5.
 */
interface NewsApi {
    suspend fun fetchLatestNews(): List<News>
}