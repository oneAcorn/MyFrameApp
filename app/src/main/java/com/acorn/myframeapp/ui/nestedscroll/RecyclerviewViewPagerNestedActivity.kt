package com.acorn.myframeapp.ui.nestedscroll

import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.acorn.basemodule.base.recyclerAdapter.BaseRecyclerAdapter
import com.acorn.myframeapp.extendfun.getRandomList
import com.acorn.myframeapp.ui.nestedscroll.view.QDContinuousBottomView
import com.acorn.myframeapp.ui.recyclerview.adapter.ConventionalRecyclerAdapter
import com.qmuiteam.qmui.nestedScroll.*
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import kotlinx.android.synthetic.main.activity_base_nested_scroll.*

/**
 * (header + recyclerView + bottom) + (part sticky header + viewpager)
 * Created by acorn on 2022/5/26.
 */
class RecyclerviewViewPagerNestedActivity : BaseNestedScrollActivity() {
    private lateinit var mTopDelegateLayout: QMUIContinuousNestedTopDelegateLayout
    private lateinit var mTopRecyclerView: QMUIContinuousNestedTopRecyclerView
    private lateinit var mBottomView: QDContinuousBottomView
    private lateinit var mAdapter: ConventionalRecyclerAdapter

    override fun initCoordinatorLayout() {
        mTopDelegateLayout = QMUIContinuousNestedTopDelegateLayout(this)
        mTopDelegateLayout.setBackgroundColor(Color.LTGRAY)
//        QMUIContinuousNestedTopRecyclerView(this)

        val headerView: AppCompatTextView = object : AppCompatTextView(this) {
            override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
                super.onMeasure(
                    widthMeasureSpec, MeasureSpec.makeMeasureSpec(
                        QMUIDisplayHelper.dp2px(context, 100), MeasureSpec.EXACTLY
                    )
                )
            }
        }
        headerView.textSize = 17f
        headerView.setBackgroundColor(Color.GRAY)
        headerView.setTextColor(Color.WHITE)
        headerView.text = "This is Top Header"
        headerView.gravity = Gravity.CENTER
        mTopDelegateLayout.setHeaderView(headerView)

        val footerView: AppCompatTextView = object : AppCompatTextView(this) {
            override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
                super.onMeasure(
                    widthMeasureSpec, MeasureSpec.makeMeasureSpec(
                        QMUIDisplayHelper.dp2px(context, 100), MeasureSpec.EXACTLY
                    )
                )
            }
        }
        footerView.textSize = 17f
        footerView.setBackgroundColor(Color.GRAY)
        footerView.setTextColor(Color.WHITE)
        footerView.gravity = Gravity.CENTER
        footerView.text = "This is Top Footer"
        mTopDelegateLayout.setFooterView(footerView)

        mTopRecyclerView = QMUIContinuousNestedTopRecyclerView(this)
        mTopRecyclerView.layoutManager = object : LinearLayoutManager(this) {
            override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
                return RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }
        mTopDelegateLayout.setDelegateView(mTopRecyclerView)

        val matchParent = ViewGroup.LayoutParams.MATCH_PARENT
        val topLp = CoordinatorLayout.LayoutParams(
            matchParent, matchParent
        )
        topLp.behavior = QMUIContinuousNestedTopAreaBehavior(this)
        coordinatorLayout.setTopAreaView(mTopDelegateLayout, topLp)

        mBottomView = QDContinuousBottomView(this)
        val recyclerViewLp = CoordinatorLayout.LayoutParams(
            matchParent, matchParent
        )
        recyclerViewLp.behavior = QMUIContinuousNestedBottomAreaBehavior()
        coordinatorLayout.setBottomAreaView(mBottomView, recyclerViewLp)

        coordinatorLayout.addOnScrollListener(object :
            QMUIContinuousNestedScrollLayout.OnScrollListener {
            override fun onScroll(
                scrollLayout: QMUIContinuousNestedScrollLayout,
                topCurrent: Int,
                topRange: Int,
                offsetCurrent: Int,
                offsetRange: Int,
                bottomCurrent: Int,
                bottomRange: Int
            ) {
                Log.i(
                    "AcornTag",
                    String.format(
                        "topCurrent = %d; topRange = %d; " +
                                "offsetCurrent = %d; offsetRange = %d; " +
                                "bottomCurrent = %d, bottomRange = %d",
                        topCurrent, topRange, offsetCurrent, offsetRange, bottomCurrent, bottomRange
                    )
                )
            }

            override fun onScrollStateChange(
                scrollLayout: QMUIContinuousNestedScrollLayout,
                newScrollState: Int,
                fromTopBehavior: Boolean
            ) {
            }
        })

        mAdapter =  ConventionalRecyclerAdapter(this, getRandomList())
        mAdapter.setOnItemClickListener(object : BaseRecyclerAdapter.OnItemClickListener {
            override fun onItemClick(itemView: View, position: Int, itemViewType: Int) {
                showTip(
                    "position:$position,itemViewType:$itemViewType,data:${
                        mAdapter?.getItem(
                            position
                        )
                    }}"
                )
            }
        })
        mTopRecyclerView.adapter = mAdapter
    }
}