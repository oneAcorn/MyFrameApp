package com.acorn.myframeapp.ui.coroutines.flow.datasource

import com.acorn.basemodule.extendfun.logI
import com.acorn.myframeapp.ui.coroutines.flow.bean.News
import com.acorn.myframeapp.ui.coroutines.flow.viewmodels.NewsApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * https://developer.android.com/kotlin/flow?hl=en
 * Created by acorn on 2023/1/5.
 */
class NewsRemoteDataSource(
    private val newsApi: NewsApi,
    private val refreshIntervalMs: Long = 5000L
) {
    val latestNews: Flow<List<News>> = flow {
        while (true) {
            val latestNews = newsApi.fetchLatestNews()
            emit(latestNews)
            logI("DataSource Flow:${Thread.currentThread()}")
            kotlinx.coroutines.delay(refreshIntervalMs)
        }
    }
}