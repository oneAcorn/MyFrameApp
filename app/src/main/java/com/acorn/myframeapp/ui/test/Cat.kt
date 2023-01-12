package com.acorn.myframeapp.ui.test

import com.acorn.basemodule.extendfun.showToast

/**
 * Created by acorn on 2023/1/12.
 */
open class Cat {
    fun bark(name: String, age: Int): String {
        showToast("$name:喵,$age")
        return "喵喵"
    }
}
