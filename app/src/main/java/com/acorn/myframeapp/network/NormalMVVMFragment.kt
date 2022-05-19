package com.acorn.myframeapp.network

import com.acorn.basemodule.base.BaseFragment
import com.acorn.basemodule.network.createViewModel
import com.acorn.myframeapp.R
import com.acorn.myframeapp.network.viewmodel.NormalViewModel
import kotlinx.android.synthetic.main.fragment_normal_mvvm.*

/**
 * Created by acorn on 2022/5/19.
 */
class NormalMVVMFragment : BaseFragment() {
    private val viewModel: NormalViewModel by lazy {
        createViewModel(
            NormalViewModel::class.java
        )
    }

    override fun getLayoutId(): Int = R.layout.fragment_normal_mvvm

    override fun initData() {
        super.initData()
        viewModel.commonState.observe(this, this)
        viewModel.getNetLiveData().observe(this) {
            netSuccessTv.text = "Request Success! ${it.data}"
        }

        refreshNet()
    }

    override fun refreshNet() {
        super.refreshNet()
        viewModel.requestNet()
    }
}