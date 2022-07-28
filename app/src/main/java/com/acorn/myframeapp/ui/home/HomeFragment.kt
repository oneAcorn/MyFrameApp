package com.acorn.myframeapp.ui.home

import android.os.Bundle
import com.acorn.myframeapp.demo.BaseDemoFragment
import com.acorn.myframeapp.demo.Demo
import com.acorn.myframeapp.ui.annotation.AnnotationActivity
import com.acorn.myframeapp.ui.dialog.DialogActivity
import com.acorn.myframeapp.ui.internationalization.InternationalActivity
import com.acorn.myframeapp.ui.lazyfragment.androidx.AndroidXLazyFragmentActivity
import com.acorn.myframeapp.ui.lazyfragment.old.OldLazyFragmentActivity
import com.acorn.myframeapp.ui.mpchart.LineChartActivity
import com.acorn.myframeapp.ui.nestedscroll.RecyclerviewViewPagerNestedActivity
import com.acorn.myframeapp.ui.nestedscroll.TwoRecyclerViewNestedActivity
import com.acorn.myframeapp.ui.nestedscroll.WebviewRecyclerNestedActivity
import com.acorn.myframeapp.ui.network.MVVMEventActivity
import com.acorn.myframeapp.ui.network.NormalMVVMActivity
import com.acorn.myframeapp.ui.network.NormalMVVMFragmentActivity
import com.acorn.myframeapp.ui.photo.TakePhotoOrVideoActivity
import com.acorn.myframeapp.ui.popup.PopupActivity
import com.acorn.myframeapp.ui.pulllayout.QMUIPullLayoutActivity
import com.acorn.myframeapp.ui.recyclerview.*
import com.acorn.myframeapp.ui.recyclerview.largedata.LargeDataRecyclerActivity
import com.acorn.myframeapp.ui.recyclerview.largedata2.LargeDataDynamicActivity
import com.acorn.myframeapp.ui.recyclerview.largedata2.LargeDataSourceDynamic
import com.acorn.myframeapp.ui.webview.X5WebviewActivity
import com.qmuiteam.qmui.skin.QMUISkinManager
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet.BottomListSheetBuilder

/**
 * Created by acorn on 2022/5/18.
 */
class HomeFragment : BaseDemoFragment() {

    companion object {
        private const val CLICK_X5_WEBVIEW_ACTIVITY = 0

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
                    ),
                    Demo(
                        "MVVM test event lost",
                        description = "Use observeForever to avoid event loosing when Activity inactive",
                        activity = MVVMEventActivity::class.java
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
                    Demo("Draggable", activity = DragRecyclerViewActivity::class.java),
                    Demo(
                        "Large Data",
                        description = "Use Google Paging",
                        activity = LargeDataRecyclerActivity::class.java
                    ),Demo(
                        "Large Data2",
                        description = "Use Google Paging with RemoteMediator",
                        activity = LargeDataDynamicActivity::class.java
                    ),
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
            Demo("Dialog", activity = DialogActivity::class.java),
            Demo("Take photo or video", activity = TakePhotoOrVideoActivity::class.java),
            Demo(
                "Webview", subItems = arrayListOf(
                    Demo("X5Webview", CLICK_X5_WEBVIEW_ACTIVITY)
                )
            ),
            Demo("Popup", activity = PopupActivity::class.java),
            Demo(
                "LazyFragment", subItems = arrayListOf(
                    Demo(
                        "OldLazyFragment",
                        description = "Lazy loading fragment in viewpager before AndroidX",
                        activity = OldLazyFragmentActivity::class.java
                    ),
                    Demo(
                        "LazyFragment",
                        description = "Google recommonds lazy loading after androidx.fragment 1.1.0",
                        activity = AndroidXLazyFragmentActivity::class.java
                    )
                )
            ),
            Demo("Annotation", activity = AnnotationActivity::class.java),
            Demo("Internationalization", activity = InternationalActivity::class.java),
            Demo(
                "MpAndroidChart", description = "https://github.com/PhilJay/MPAndroidChart",
                subItems = arrayListOf(
                    Demo("LineChart", activity = LineChartActivity::class.java)
                )
            )
        )
    }

    override fun onItemClick(data: Demo, idOrPosition: Int) {
        when (idOrPosition) {
            CLICK_X5_WEBVIEW_ACTIVITY -> {
                X5WebviewActivity.open(
                    requireActivity(),
                    "http://jandan.net/p/110765",
                    "这是标题",
                    false
                )
            }
            else -> {}
        }
    }
}