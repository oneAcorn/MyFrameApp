package com.acorn.myframeapp.ui.coroutines.flow.bean

/**
 * Created by acorn on 2023/1/5.
 */
data class UserData(val userName:String) {
    fun isFavoriteTopic(topic: String): Boolean = true
}