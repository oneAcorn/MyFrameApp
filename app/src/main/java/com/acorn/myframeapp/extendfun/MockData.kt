package com.acorn.myframeapp.extendfun

import kotlin.random.Random

/**
 * Created by acorn on 2022/5/24.
 */

/**
 * @param randomCountUntil 随机数量的最大值
 */
fun getRandomList(randomCountUntil: Int = 100): List<String> {
    val list = mutableListOf<String>()
    for (i in 0..Random.nextInt(randomCountUntil)) {
        list.add(randomItemString())
    }
    return list
}

fun randomItemString(): String = "RandomItem:${Random.nextInt(200)}"