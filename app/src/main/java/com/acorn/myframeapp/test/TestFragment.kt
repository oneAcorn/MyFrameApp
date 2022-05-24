package com.acorn.myframeapp.test

import android.os.Bundle
import com.acorn.basemodule.base.BaseFragment
import com.acorn.basemodule.extendfun.logI
import com.acorn.basemodule.network.BaseNetViewModel
import com.acorn.myframeapp.R
import com.acorn.myframeapp.base.AppBaseFragment
import com.acorn.myframeapp.demo.BaseDemoFragment
import com.acorn.myframeapp.demo.Demo

/**
 * Created by acorn on 2022/5/18.
 */
class TestFragment : BaseDemoFragment() {

    companion object {
        fun newInstance(): TestFragment {
            val args = Bundle()

            val fragment = TestFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getItems(): Array<Demo> {
        return arrayOf(Demo("Nothing right now"))
    }

    override fun onItemClick(data: Demo, idOrPosition: Int) {
    }

}