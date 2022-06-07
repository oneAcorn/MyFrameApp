package com.acorn.myframeapp.ui.lazyfragment.androidx

import android.os.Bundle
import com.acorn.basemodule.base.BaseFragment
import com.acorn.basemodule.extendfun.logI
import com.acorn.basemodule.extendfun.sp
import com.acorn.basemodule.network.BaseNetViewModel
import com.acorn.myframeapp.R
import kotlinx.android.synthetic.main.fragment_my_lazy_fragment.*

/**
 * Created by acorn on 2022/6/7.
 */
class MyAndroidXLazyFragment1 : BaseFragment<BaseNetViewModel>() {
    private var name: String? = null

    companion object {
        fun newInstance(name: String): MyAndroidXLazyFragment1 {
            val args = Bundle()
            args.putString("name", name)
            val fragment = MyAndroidXLazyFragment1()
            fragment.arguments = args
            return fragment
        }
    }

    override fun initView() {
        super.initView()
        name = arguments?.getString("name")
        tv.text = "I'm $name"
        tv2.text =
            "在androidx.fragment 1.1.0之后通过BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT可以直接在onResume中判断Fragment可见状态"
    }

    override fun onResume() {
        super.onResume()
        logI("$name OnResume")
    }

    override fun getLayoutId(): Int = R.layout.fragment_my_lazy_fragment

    override fun getViewModel(): BaseNetViewModel? = null
}