package com.acorn.myframeapp.ui.coroutines.flow.viewmodels

import com.acorn.myframeapp.ui.coroutines.flow.bean.News
import kotlin.random.Random

/**
 * Created by acorn on 2023/1/6.
 */
class NewsApiImpl : NewsApi {
    override suspend fun fetchLatestNews(): List<News> {
        val size = Random.nextInt(1, 5)
        val newsList = mutableListOf<News>()
        for (i in 0..size) {
            val news = News("whatever", "content${Random.nextInt(5000)}")
            newsList.add(news)
        }
        return newsList
    }
}