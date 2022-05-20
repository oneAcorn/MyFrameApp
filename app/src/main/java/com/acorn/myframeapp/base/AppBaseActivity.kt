package com.acorn.myframeapp.base

import com.acorn.basemodule.base.BaseActivity
import com.acorn.basemodule.network.BaseNetViewModel

/**
 * Created by acorn on 2022/5/20.
 */
abstract class AppBaseActivity<T : BaseNetViewModel> : BaseActivity<T>() {

}