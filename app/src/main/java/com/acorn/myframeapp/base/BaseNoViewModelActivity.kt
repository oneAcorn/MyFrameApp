package com.acorn.myframeapp.base

import com.acorn.basemodule.base.BaseActivity
import com.acorn.basemodule.network.BaseNetViewModel

/**
 * Created by acorn on 2022/5/24.
 */
abstract class BaseNoViewModelActivity : BaseActivity<BaseNetViewModel>() {
    override fun getViewModel(): BaseNetViewModel? = null
}