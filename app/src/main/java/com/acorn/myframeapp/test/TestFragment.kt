package com.acorn.myframeapp.test

import android.os.Bundle
import android.view.View
import com.acorn.basemodule.BaseFragment
import com.acorn.basemodule.extendfun.logI
import com.acorn.myframeapp.R

/**
 * Created by acorn on 2022/5/18.
 */
class TestFragment : BaseFragment() {

    companion object {
        fun newInstance(): TestFragment {
            val args = Bundle()

            val fragment = TestFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_test
    }

    override fun initData() {
        logI("TestFragment:initData")
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        logI("TestFragment setUserVisibleHint:$isVisibleToUser")
    }

}