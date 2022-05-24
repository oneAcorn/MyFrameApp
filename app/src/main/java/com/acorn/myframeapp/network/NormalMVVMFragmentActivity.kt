package com.acorn.myframeapp.network

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.acorn.basemodule.base.BaseActivity
import com.acorn.basemodule.network.BaseNetViewModel
import com.acorn.myframeapp.R
import com.acorn.myframeapp.base.AppBaseActivity

/**
 * Created by acorn on 2022/5/19.
 */
class NormalMVVMFragmentActivity : AppBaseActivity<BaseNetViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_normal_mvvm_fragment)
        showToolbar {
            it.title = "NormalMVVM"
        }
    }

    override fun getViewModel(): BaseNetViewModel? = null
}