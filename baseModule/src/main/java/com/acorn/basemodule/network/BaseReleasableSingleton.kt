package com.acorn.basemodule.network


/**
 * 线程安全的懒汉式单例，相比直接用lazy的Synchronized模式，此实现可以将单例回收。
 * Created by acorn on 2020/6/11.
 */
abstract class BaseReleasableSingleton<T> {
    private var instance: T? = null
        get() {
            if (field == null) {
                field = newInstance()
            }
            return field
        }

    protected abstract fun newInstance(): T

    @Synchronized
    fun get(): T {
        return instance!!
    }

    @Synchronized
    fun release() {
        instance = null
    }
}