package com.acorn.myframeapp.ui.test

import android.os.Bundle
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
        return arrayOf(Demo("TestView", activity = TestViewActivity::class.java))
    }

    override fun onItemClick(data: Demo, idOrPosition: Int) {
    }

}