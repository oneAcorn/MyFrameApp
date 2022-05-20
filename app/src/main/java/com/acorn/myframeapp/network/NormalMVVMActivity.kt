package com.acorn.myframeapp.network

import android.os.Bundle
import com.acorn.basemodule.base.BaseActivity
import com.acorn.basemodule.network.createViewModel
import com.acorn.myframeapp.R
import com.acorn.myframeapp.network.viewmodel.NormalViewModel
import kotlinx.android.synthetic.main.fragment_normal_mvvm.*

/**
 * Created by acorn on 2022/5/20.
 */
class NormalMVVMActivity : BaseActivity<NormalViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_normal_mvvm)
    }

    override fun initData() {
        super.initData()
        mViewModel?.getNetLiveData()?.observe(this) {
            netSuccessTv.text = "Request Success! ${it.data}"
        }

        refreshNet()
    }

    override fun getViewModel(): NormalViewModel = createViewModel(NormalViewModel::class.java)

    override fun isEmbedInBaseLayout(): Boolean {
        return true
    }

    override fun refreshNet() {
        super.refreshNet()
        mViewModel?.requestNet()
    }
}