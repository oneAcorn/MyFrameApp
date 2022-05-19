package com.acorn.myframeapp.home

import android.os.Bundle
import com.acorn.myframeapp.demo.BaseDemoFragment
import com.acorn.myframeapp.demo.Demo
import com.acorn.myframeapp.network.NormalMVVMFragmentActivity

/**
 * Created by acorn on 2022/5/18.
 */
class HomeFragment : BaseDemoFragment() {

    companion object {
        fun newInstance(): HomeFragment {
            val args = Bundle()

            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getItems(): Array<Demo> {
        return arrayOf(
            Demo(
                "Network",
                subItems = arrayListOf(
                    Demo(
                        "Normal MVVM Use",
                        description = "Error Layout(retryBtn),Empty Layout,LoadingDialog",
                        activity = NormalMVVMFragmentActivity::class.java
                    )
                )
            )
        )
    }

    override fun onItemClick(data: Demo, idOrPosition: Int) {
    }

}