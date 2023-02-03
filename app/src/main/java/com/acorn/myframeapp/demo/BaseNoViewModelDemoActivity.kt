package com.acorn.myframeapp.demo

import com.acorn.basemodule.network.BaseNetViewModel

/**
 * Created by acorn on 2023/2/3.
 */
abstract class BaseNoViewModelDemoActivity :BaseDemoActivity<BaseNetViewModel>(){
    override fun getViewModel(): BaseNetViewModel? {
        return null
    }
}