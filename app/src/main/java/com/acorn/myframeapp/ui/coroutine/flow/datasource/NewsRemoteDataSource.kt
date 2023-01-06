package com.acorn.myframeapp.ui.coroutine.flow.datasource

import com.acorn.myframeapp.ui.coroutine.flow.bean.News
import com.acorn.myframeapp.ui.coroutine.flow.viewmodels.NewsApi
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
            kotlinx.coroutines.delay(refreshIntervalMs)
        }
    }
}