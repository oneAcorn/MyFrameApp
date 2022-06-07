package com.acorn.myframeapp.ui.lazyfragment.old

import android.os.Bundle
import android.view.View
import com.acorn.basemodule.network.BaseNetViewModel
import com.acorn.myframeapp.R
import kotlinx.android.synthetic.main.fragment_my_lazy_fragment.view.*

/**
 * Created by acorn on 2022/6/7.
 */
class MyFragment1 : LazyFragment<BaseNetViewModel>() {
    private var name: String? = null

    companion object {
        fun newInstance(name: String): MyFragment1 {
            val args = Bundle()
            args.putString("name", name)
            val fragment = MyFragment1()
            fragment.arguments = args
            return fragment
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        //For Test Log
        name = arguments?.getString("name")
        tagName = name
        super.setUserVisibleHint(isVisibleToUser)
    }

    override fun onFragmentFirstVisible() {
//        logI("$name onFragmentFirstVisible")
    }

    override fun initView(rootView: View?) {
        rootView ?: return
        rootView.tv.text = "I'm $name"
    }

    override fun getLayoutRes(): Int = R.layout.fragment_my_lazy_fragment

    override fun onFragmentResume() {
        super.onFragmentResume()
//        logI("$name onFragmentResume")
    }

    override fun onFragmentPause() {
        super.onFragmentPause()
//        logI("$name onFragmentPause")
    }
}