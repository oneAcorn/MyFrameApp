package com.acorn.myframeapp.ui.recyclerview

import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.acorn.myframeapp.R
import com.acorn.myframeapp.base.BaseNoViewModelActivity
import com.acorn.myframeapp.ui.recyclerview.adapter.StickySectionRecyclerAdapter
import com.acorn.myframeapp.ui.recyclerview.data.SectionHeader
import com.acorn.myframeapp.ui.recyclerview.data.SectionItem
import com.qmuiteam.qmui.recyclerView.QMUIRVDraggableScrollBar
import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIPullRefreshLayout
import com.qmuiteam.qmui.widget.section.QMUISection
import com.qmuiteam.qmui.widget.section.QMUIStickySectionAdapter
import kotlinx.android.synthetic.main.activity_sticky_section.*

/**
 * Created by acorn on 2022/5/25.
 */
class StickySectionRecyclerViewActivity : BaseNoViewModelActivity() {
    private var mAdapter: StickySectionRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sticky_section)
    }

    override fun initView() {
        super.initView()
        showToolbar { it.title = "StickySection" }
        initRefreshLayout()
        initStickyLayout()
    }

    override fun initData() {
        super.initData()
        mAdapter = StickySectionRecyclerAdapter().apply {
            setCallback(object : QMUIStickySectionAdapter.Callback<SectionHeader, SectionItem> {
                override fun loadMore(
                    section: QMUISection<SectionHeader, SectionItem>?,
                    loadMoreBefore: Boolean
                ) {
                    sectionLayout.postDelayed({
                        if (isFinishing)
                            return@postDelayed
                        val moreList = mutableListOf<SectionItem>()
                        for (i in 0..9) {
                            moreList.add(SectionItem("load more item $i"))
                        }
                        mAdapter?.finishLoadMore(section, moreList, loadMoreBefore, false)
                    }, 1200)
                }

                override fun onItemClick(
                    holder: QMUIStickySectionAdapter.ViewHolder?,
                    position: Int
                ) {
                    showTip("click position:$position")
                }

                override fun onItemLongClick(
                    holder: QMUIStickySectionAdapter.ViewHolder?,
                    position: Int
                ): Boolean {
                    showTip("LongClick position:$position")
                    return true
                }

            })
        }
        sectionLayout.setAdapter(mAdapter, true)
        val list = mutableListOf<QMUISection<SectionHeader, SectionItem>>()
        for (i in 0..20) {
            list.add(createSection("header$i", i % 2 != 0))
        }
        mAdapter?.setData(list)
    }

    private fun createSection(
        headerText: String,
        isFold: Boolean
    ): QMUISection<SectionHeader, SectionItem> {
        val header = SectionHeader(headerText)
        val contents: ArrayList<SectionItem> = ArrayList()
        for (i in 0..19) {
            contents.add(SectionItem("item $i"))
        }
        val section: QMUISection<SectionHeader, SectionItem> =
            QMUISection(header, contents, isFold)
        // if test load more, you can open the code
        section.isExistAfterDataToLoad = true
//        section.isExistBeforeDataToLoad = true;
        return section
    }

    private fun initRefreshLayout() {
        pullRefreshLayout.setOnPullListener(object : QMUIPullRefreshLayout.OnPullListener {
            override fun onMoveTarget(offset: Int) {

            }

            override fun onMoveRefreshView(offset: Int) {
            }

            override fun onRefresh() {
                pullRefreshLayout.postDelayed({
                    pullRefreshLayout.finishRefresh()
                }, 1200)
            }
        })
    }

    private fun initStickyLayout() {
        sectionLayout.setLayoutManager(
            object : LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
            ) {
//                override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
//                    return RecyclerView.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
//                    )
//                }
            }
        )
//        val scrollBar = QMUIRVDraggableScrollBar(0, 0, 0)
//        scrollBar.isEnableScrollBarFadeInOut = false
//        scrollBar.attachToStickSectionLayout(sectionLayout)
    }
}