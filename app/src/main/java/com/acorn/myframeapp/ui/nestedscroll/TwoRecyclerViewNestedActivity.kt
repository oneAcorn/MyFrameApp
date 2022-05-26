package com.acorn.myframeapp.ui.nestedscroll

import android.R
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.acorn.basemodule.base.recyclerAdapter.BaseRecyclerAdapter
import com.acorn.myframeapp.extendfun.getRandomList
import com.acorn.myframeapp.ui.recyclerview.adapter.ConventionalRecyclerAdapter
import com.qmuiteam.qmui.nestedScroll.*
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import kotlinx.android.synthetic.main.activity_base_nested_scroll.*

/**
 * (Header+RecyclerView+Footer)+RecyclerView
 * Created by acorn on 2022/5/26.
 */
class TwoRecyclerViewNestedActivity : BaseNestedScrollActivity() {
    private lateinit var mTopDelegateLayout: QMUIContinuousNestedTopDelegateLayout
    private lateinit var mTopRecyclerView: QMUIContinuousNestedTopRecyclerView
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: ConventionalRecyclerAdapter

    override fun initCoordinatorLayout() {
        mTopDelegateLayout = QMUIContinuousNestedTopDelegateLayout(this)
        mTopDelegateLayout.setBackgroundColor(Color.LTGRAY)
        mTopRecyclerView = QMUIContinuousNestedTopRecyclerView(this)

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

        mTopDelegateLayout.setDelegateView(mTopRecyclerView)


        val matchParent = ViewGroup.LayoutParams.MATCH_PARENT
        val topLp = CoordinatorLayout.LayoutParams(
            matchParent, matchParent
        )
        topLp.behavior = QMUIContinuousNestedTopAreaBehavior(this)
        coordinatorLayout.setTopAreaView(mTopDelegateLayout, topLp)

        mRecyclerView = QMUIContinuousNestedBottomRecyclerView(this)
        val recyclerViewLp = CoordinatorLayout.LayoutParams(
            matchParent, matchParent
        )
        recyclerViewLp.behavior = QMUIContinuousNestedBottomAreaBehavior()
        coordinatorLayout.setBottomAreaView(mRecyclerView, recyclerViewLp)

        mTopRecyclerView.layoutManager = object : LinearLayoutManager(this) {
            override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
                return RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        mRecyclerView.layoutManager = object : LinearLayoutManager(this) {
            override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
                return RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

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
        mRecyclerView.adapter = mAdapter
    }
}