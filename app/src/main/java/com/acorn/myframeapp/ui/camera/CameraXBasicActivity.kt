package com.acorn.myframeapp.ui.camera

import com.acorn.basemodule.base.BaseBindingActivity
import com.acorn.basemodule.network.BaseNetViewModel
import com.acorn.myframeapp.databinding.ActivityCameraxBasicBinding

/**
 * Created by acorn on 2023/1/11.
 */
class CameraXBasicActivity : BaseBindingActivity<BaseNetViewModel, ActivityCameraxBasicBinding>() {



    override fun getViewModel(): BaseNetViewModel? = null

    override fun createBinding(): ActivityCameraxBasicBinding =
        ActivityCameraxBasicBinding.inflate(layoutInflater)
}