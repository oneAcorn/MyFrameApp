package com.acorn.myframeapp.ui.coroutine.flow.viewmodels

import com.acorn.myframeapp.ui.coroutine.flow.bean.News

/**
 * Created by acorn on 2023/1/5.
 */
interface NewsApi {
    suspend fun fetchLatestNews(): List<News>
}