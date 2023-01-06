package com.acorn.myframeapp.ui.coroutine.flow.viewmodels

import com.acorn.myframeapp.ui.coroutine.flow.bean.News

/**
 * Created by acorn on 2023/1/6.
 */
class NewsApiImpl:NewsApi {
    override suspend fun fetchLatestNews(): List<News> {
        TODO("Not yet implemented")
    }
}