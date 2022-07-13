package com.acorn.myframeapp.ui.network

import android.os.Bundle
import com.acorn.basemodule.network.createViewModel
import com.acorn.myframeapp.R
import com.acorn.myframeapp.base.AppBaseActivity
import com.acorn.myframeapp.ui.network.viewmodel.EventViewModel
import kotlinx.android.synthetic.main.activity_mvvm_event.*

/**
 * Created by acorn on 2022/7/13.
 */
class MVVMEventActivity : AppBaseActivity<EventViewModel>() {
    private val sb = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvvm_event)
    }

    override fun initData() {
        super.initData()
        //和生命周期无关的观察者
        mViewModel?.getIntervalLiveData()?.observeForever {
            sb.append(it)
            sb.append(",")
            eventTv.text = sb.toString()
        }
        mViewModel?.intervalEvent()
    }

    override fun getViewModel(): EventViewModel = createViewModel(EventViewModel::class.java)
}