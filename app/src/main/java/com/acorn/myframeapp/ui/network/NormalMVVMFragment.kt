package com.acorn.myframeapp.ui.network

import com.acorn.basemodule.network.createViewModel
import com.acorn.myframeapp.R
import com.acorn.myframeapp.base.AppBaseFragment
import com.acorn.myframeapp.ui.network.viewmodel.NormalViewModel
import kotlinx.android.synthetic.main.fragment_normal_mvvm.*

/**
 * Created by acorn on 2022/5/19.
 */
class NormalMVVMFragment : AppBaseFragment<NormalViewModel>() {

    override fun getLayoutId(): Int = R.layout.fragment_normal_mvvm

    override fun initData() {
        super.initData()
        mViewModel?.getNetLiveData()?.observe(this) {
            netSuccessTv.text = "Request Success! ${it.data}"
        }

        refreshNet()
    }

    override fun refreshNet() {
        super.refreshNet()
        mViewModel?.requestNet()
    }

    override fun getViewModel(): NormalViewModel = createViewModel(NormalViewModel::class.java)

    override fun isEmbedInBaseLayout(): Boolean = true
}