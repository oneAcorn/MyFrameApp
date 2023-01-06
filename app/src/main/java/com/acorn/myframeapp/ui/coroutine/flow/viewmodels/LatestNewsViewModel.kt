package com.acorn.myframeapp.ui.coroutine.flow.viewmodels

import androidx.lifecycle.viewModelScope
import com.acorn.basemodule.network.BaseNetViewModel
import com.acorn.myframeapp.ui.coroutine.flow.bean.News
import com.acorn.myframeapp.ui.coroutine.flow.bean.UserData
import com.acorn.myframeapp.ui.coroutine.flow.datasource.NewsRemoteDataSource
import com.acorn.myframeapp.ui.coroutine.flow.datasource.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * https://developer.android.com/kotlin/flow?hl=en
 * Created by acorn on 2023/1/6.
 */
class LatestNewsViewModel : BaseNetViewModel() {
    private val newsRepository: NewsRepository = NewsRepository(
        NewsRemoteDataSource(NewsApiImpl()),
        UserData("老张"), Dispatchers.Main
    )

    /**
     * LiveData 与 StateFlow 的区别
    关于 LiveData 与 StateFlow，网上说的最多的区别，其实就是「官方指导文档」中所讲解的这两点，这里我直接搬运过来：
    1. StateFlow 需要将初始状态传递给构造函数，而 LiveData 不需要。
    2. 当 View 进入 STOPPED 状态时，LiveData.observe() 会自动取消注册使用方，而从 StateFlow 或任何其他数据流收集数据的操作并不会自动停止。
    如需实现相同的行为，您需要从 Lifecycle.repeatOnLifecycle 块收集数据流。
     */
    //https://blog.csdn.net/weixin_61845324/article/details/125203461
    // Backing property to avoid state updates from other classes
    private val _uiState = MutableStateFlow(LatestNewsUiState.Success(emptyList()))

    // The UI collects from this StateFlow to get its state updates
    val uiState: StateFlow<LatestNewsUiState> = _uiState

    init {
        viewModelScope.launch {
            // Trigger the flow and consume its elements using collect
            newsRepository.favoriteLatestNews
                // Intermediate catch operator. If an exception is thrown,
                // catch and update the UI
                .catch { exception -> exception.printStackTrace() }
                .collect { favoriteNews ->
                    // Update View with the latest favorite news
                    _uiState.value = LatestNewsUiState.Success(favoriteNews)
                }
        }
    }
}

// Represents different states for the LatestNews screen
sealed class LatestNewsUiState {
    data class Success(val news: List<News>) : LatestNewsUiState()
    data class Error(val exception: Throwable) : LatestNewsUiState()
}