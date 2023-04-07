package com.acorn.myframeapp.ui.coroutines.test

import com.acorn.basemodule.extendfun.logI
import kotlinx.coroutines.*

/**
 * Created by acorn on 2023/2/17.
 */
suspend fun main() {
    println("主线程开始 ${Thread.currentThread()}")

//    testGlobal()
//    testSuspend()
//    testChildJob()
//    testChildJob2()
//    getUserInfo()
//    testMillisDuration()
    testJobCancel()

    println("主线程结束")
    //这里的sleep只是保持进程存活, 目的是为了等待协程执行完
    Thread.sleep(10000)
}

//region testGlobal

/**
 * Job中常用的方法
job.isActive
job.isCancelled
job.isCompleted
job.cancel()
job.join()
 */
private fun testGlobal() {
    //作用域可以用使用GlobalScope(不推荐使用)、lifecycleScope、viewModelScope等,
    //这里使用GlobalScope只是方便演示
    val job = GlobalScope.launch(Dispatchers.Default) {
        repeat(5) {
            println("Global 协程:${Thread.currentThread()}")
            delay(500)
        }
    }
}
//endregion

//region testSuspend

private fun testSuspend() {
    GlobalScope.launch {
        //开始时间
        val startTime = System.currentTimeMillis()

        //模拟异步获取请求1
        val result1 = async {
            getResult1()
        }
        //模拟异步获取请求2
        val result2 = async {
            getResult2()
        }
        //合并2者请求结果
        val result = result1.await() + "+" + result2.await()
        println("result =    $result")

        //结束时间
        val endTime = System.currentTimeMillis()
        println("time =    ${endTime - startTime}")
    }
}

private suspend fun getResult1(): String {
    delay(3000)
    return "result1"
}

private suspend fun getResult2(): String {
    delay(4000)
    return "result2"
}
//endregion

//region testChildJob

private fun testChildJob() {
    //如果不使用SupervisorJob,那么只要其中一个子协程崩溃,其他子协程就会自动取消
    val scope = CoroutineScope(SupervisorJob())
    //子线程1崩溃会导致其他子线程取消
//    val scope= CoroutineScope(Job())

    scope.launch {//child1 子协程1
        println("Child1")
        throw error("child1 error")
    }

    scope.launch { //child2 子协程2
        println("Child2")
        delay(1000)
        println("Child2 finish")
    }
}

/**
 * 另一种写法
 */
private suspend fun testChildJob2() {
    supervisorScope {
        launch {
            // Child 1
            println("Child 1")
            throw error("Child 1 error ")
        }
        launch {
            // Child 2
            println("Child 2  before")
            delay(1000)
            println("Child 2")
        }
    }
}
//endregion

//region testWithContext

private suspend fun testWithContext() {
    getUserInfo()
}

private suspend fun getUserInfo() {       // Dispatchers.Main
    val data = fetchUserInfo()    // Dispatchers.Main
    println("主线程执行UI相关操作:${Thread.currentThread()}") // Dispatchers.Main
}


private suspend fun fetchUserInfo() {     // Dispatchers.Main
    println("这里还是主线程:${Thread.currentThread()}") // Dispatchers.Main
    withContext(Dispatchers.IO) { // Dispatchers.IO
        println("这里是子线程了:${Thread.currentThread()}")  // Dispatchers.IO
    }                             // Dispatchers.Main
}
//endregion

private fun testMillisDuration() {
    GlobalScope.launch(Dispatchers.IO) {
        val beginTime = System.currentTimeMillis()
        delay(100)
        println("1")
        withContext(Dispatchers.Default) {
            println("2")
            delay(200)
        }
        println("3")
        val endTimes = System.currentTimeMillis()
        println("duration:${endTimes - beginTime}")
    }
}


private fun testJobCancel() {
    val job = GlobalScope.launch {
        while (true) {
            println("I'm alive")
            delay(100)
        }
    }
    GlobalScope.launch {
        println("scope2 start")
        delay(1000)
        job.cancel()
        println("scope2 alive")
        delay(200)
        println("scope2 still alive")
    }
}