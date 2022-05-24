package com.acorn.myframeapp.ui.home

import android.os.Bundle
import com.acorn.myframeapp.demo.BaseDemoFragment
import com.acorn.myframeapp.demo.Demo
import com.acorn.myframeapp.ui.network.NormalMVVMActivity
import com.acorn.myframeapp.ui.network.NormalMVVMFragmentActivity
import com.acorn.myframeapp.ui.recyclerview.*

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
                        "Normal MVVM Use In Fragment",
                        description = "Error Layout(retryBtn),Empty Layout,LoadingDialog",
                        activity = NormalMVVMFragmentActivity::class.java
                    ),
                    Demo(
                        "Normal MVVM Use In Activity",
                        description = "Error Layout(retryBtn),Empty Layout,LoadingDialog,TitleBar",
                        activity = NormalMVVMActivity::class.java
                    )
                )
            ),
            Demo(
                "RecyclerView",
                subItems = arrayListOf(
                    Demo(
                        "Conventional use of RecyclerView",
                        activity = ConventionalRecyclerViewActivity::class.java
                    ),
                    Demo(
                        "With Header & Footer",
                        activity = HeaderFooterRecyclerViewActivity::class.java
                    ),
                    Demo(
                        "Empty View",
                        description = "Include EmptyData,LoadingData,Error View",
                        activity = EmptyRecyclerViewActivity::class.java
                    ),
                    Demo("Animation", activity = AnimationRecyclerViewActivity::class.java),
                    Demo(
                        "GridLayout",
                        description = "Multiple ItemType,SpanSize",
                        activity = GridRecyclerViewActivity::class.java
                    ),
                    Demo(
                        "Paloads",
                        description = "Refresh Single View In Item ViewHolder",
                        activity = PayloadsRecyclerViewActivity::class.java
                    )
                )
            )
        )
    }

    override fun onItemClick(data: Demo, idOrPosition: Int) {
    }
}