package com.acorn.myframeapp.network

import com.acorn.myframeapp.demo.BaseDemoActivity
import com.acorn.myframeapp.demo.Demo

/**
 * Created by acorn on 2022/5/19.
 */
class NetworkActivity : BaseDemoActivity() {
    override fun getItems(): Array<Demo> {
        return arrayOf(
            Demo(
                "Normal MVVM Use",
                description = "Error Layout(retryBtn),Empty Layout,LoadingDialog"
            )
        )
    }

    override fun onItemClick(data: Demo, idOrPosition: Int) {
    }


}