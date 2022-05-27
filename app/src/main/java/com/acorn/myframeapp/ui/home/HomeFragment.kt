package com.acorn.myframeapp.ui.home

import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.acorn.myframeapp.R
import com.acorn.myframeapp.demo.BaseDemoFragment
import com.acorn.myframeapp.demo.Demo
import com.acorn.myframeapp.ui.dialog.DialogActivity
import com.acorn.myframeapp.ui.nestedscroll.RecyclerviewViewPagerNestedActivity
import com.acorn.myframeapp.ui.nestedscroll.TwoRecyclerViewNestedActivity
import com.acorn.myframeapp.ui.nestedscroll.WebviewRecyclerNestedActivity
import com.acorn.myframeapp.ui.network.NormalMVVMActivity
import com.acorn.myframeapp.ui.network.NormalMVVMFragmentActivity
import com.acorn.myframeapp.ui.pulllayout.QMUIPullLayoutActivity
import com.acorn.myframeapp.ui.recyclerview.*
import com.qmuiteam.qmui.skin.QMUISkinManager
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet.BottomListSheetBuilder

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
                    ),
                    Demo(
                        "ItemDecoration",
                        activity = ItemDecorationRecyclerViewActivity::class.java
                    ),
                    Demo(
                        "SwipeAction",
                        description = "swipe item to show more operation",
                        activity = SwipeActionRecyclerActivity::class.java
                    ), Demo(
                        "SwipeAction With One Action",
                        description = "swipe delete when only one action",
                        activity = SwipeSingleActionRecyclerActivity::class.java
                    ),
                    Demo(
                        "Sticky Section",
                        activity = StickySectionRecyclerViewActivity::class.java
                    ),
                    Demo("Draggable", activity = DragRecyclerViewActivity::class.java)
                )
            ),
            Demo(
                "PullLayout",
                description = "Pull to refresh,drag down to load more",
                subItems = arrayListOf(
                    Demo(
                        "QMUI PullLayout",
                        description = "For more demo,pls see QMUI_Android->QDPullFragment",
                        activity = QMUIPullLayoutActivity::class.java
                    )
                )
            ),
            Demo(
                "NestedScroll",
                description = "all kinds of nested scroll,For more combinations,pls see QMUI_Android->QDContinuousNestedScrollFragment",
                subItems = arrayListOf(
                    Demo(
                        "Webview+RecyclerView",
                        activity = WebviewRecyclerNestedActivity::class.java
                    ),
                    Demo(
                        "(Header+RecyclerView+Footer)+RecyclerView",
                        activity = TwoRecyclerViewNestedActivity::class.java
                    ),
                    Demo(
                        "(header + recyclerView + bottom) + (part sticky header + viewpager)",
                        activity = RecyclerviewViewPagerNestedActivity::class.java
                    )
                )
            ),
            Demo("Dialog", activity = DialogActivity::class.java)
        )
    }

    override fun onItemClick(data: Demo, idOrPosition: Int) {

    }
}