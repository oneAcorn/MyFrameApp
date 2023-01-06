package com.acorn.myframeapp.ui.coroutine.flow.datasource

import com.acorn.myframeapp.ui.coroutine.flow.bean.News
import com.acorn.myframeapp.ui.coroutine.flow.bean.UserData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*

/**
 * Created by acorn on 2023/1/5.
 */
class NewsRepository(
    private val newsRemoteDataSource: NewsRemoteDataSource,
    private val userData: UserData,
    private val defaultDispatcher:CoroutineDispatcher
) {
    private var _latestNewsList: List<News>? = null

    val favoriteLatestNews: Flow<List<News>> =
        newsRemoteDataSource.latestNews
            .map { newsList -> newsList.filter { userData.isFavoriteTopic(it.topic) } }
            .onEach { newsList -> _latestNewsList = newsList }
            // flowOn affects the upstream flow ↑
            .flowOn(defaultDispatcher)
            // the downstream flow ↓ is not affected
            // If an error happens, emit the last cached values
            .catch { exception -> _latestNewsList?.let { emit(it) } }
}