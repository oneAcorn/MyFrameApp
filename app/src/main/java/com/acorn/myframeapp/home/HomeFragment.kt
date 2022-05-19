package com.acorn.myframeapp.home

import android.os.Bundle
import android.view.View
import com.acorn.basemodule.BaseFragment
import com.acorn.basemodule.extendfun.logI
import com.acorn.myframeapp.R

/**
 * Created by acorn on 2022/5/18.
 */
class HomeFragment : BaseFragment() {

    companion object {
        fun newInstance(): HomeFragment {
            val args = Bundle()

            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun initData() {
        logI("HomeFragment:initData")
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        logI("HomeFragment setUserVisibleHint:$isVisibleToUser")
    }

    override fun onResume() {
        super.onResume()
        logI("HomeFragment onResume")
    }
}