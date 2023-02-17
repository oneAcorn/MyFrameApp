package com.acorn.myframeapp.ui.coroutines.normal.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

/**
 * 手动实现的ViewModelScope
 * Created by acorn on 2023/2/17.
 */
class ViewModelScopeImplViewModel:ViewModel() {
    /**
     * 这是此 ViewModel 运行的所有协程所用的任务。
     * 终止这个任务将会终止此 ViewModel 开始的所有协程。
     */
    private val viewModelJob = SupervisorJob()

    /**
     * 这是 MainViewModel 启动的所有协程的主作用域。
     * 因为我们传入了 viewModelJob，你可以通过调用viewModelJob.cancel()
     * 来取消所有 uiScope 启动的协程。
     */
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    /**
     * 当 ViewModel 清空时取消所有协程
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    /**
     * 没法在主线程完成的繁重操作
     */
    fun launchDataLoad() {
        uiScope.launch {
            sortList()
            // 更新 UI
        }
    }

    suspend fun sortList() = withContext(Dispatchers.Default) {
        // 繁重任务
    }
}