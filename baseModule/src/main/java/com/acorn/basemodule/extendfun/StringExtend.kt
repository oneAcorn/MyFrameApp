package com.acorn.basemodule.extendfun

/**
 * Created by acorn on 2022/8/9.
 */
fun Any.toStringOrNull(): String? {
    val ret = toString()
    if (ret.isEmpty()) {
        return null
    }
    return ret
}