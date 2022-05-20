package com.acorn.myframeapp.network

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.acorn.basemodule.base.BaseActivity
import com.acorn.basemodule.network.BaseNetViewModel
import com.acorn.myframeapp.R

/**
 * Created by acorn on 2022/5/19.
 */
class NormalMVVMFragmentActivity : BaseActivity<BaseNetViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_normal_mvvm_fragment)
    }

    override fun getViewModel(): BaseNetViewModel? = null
}