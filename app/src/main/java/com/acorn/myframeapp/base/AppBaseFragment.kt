package com.acorn.myframeapp.base

import com.acorn.basemodule.base.BaseFragment
import com.acorn.basemodule.network.BaseNetViewModel

/**
 * Created by acorn on 2022/5/20.
 */
abstract class AppBaseFragment<T : BaseNetViewModel> : BaseFragment<T>() {

}