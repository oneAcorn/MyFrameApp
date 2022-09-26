package com.acorn.myframeapp.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.acorn.basemodule.base.BaseBottomSheetDialog
import com.acorn.basemodule.base.recyclerAdapter.BaseRecyclerAdapter
import com.acorn.basemodule.extendfun.screenHeight
import com.acorn.basemodule.extendfun.showToast
import com.acorn.myframeapp.R
import com.acorn.myframeapp.extendfun.getRandomList
import com.acorn.myframeapp.ui.recyclerview.adapter.ConventionalRecyclerAdapter
import kotlinx.android.synthetic.main.activity_conventional_recyclerview.view.*

/**
 * 继承谷歌的BottomSheetDialogFragment
 * Created by acorn on 2022/5/27.
 */
class BottomSheetDialog1 : BaseBottomSheetDialog() {
    private var mAdapter: ConventionalRecyclerAdapter? = null

    companion object {
        fun newInstance(): BottomSheetDialog1 {
            val args = Bundle()
            val fragment = BottomSheetDialog1()
            fragment.arguments = args
            return fragment
        }
    }

    override fun initView(rootView: View) {
        rootView.rv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mAdapter = ConventionalRecyclerAdapter(requireContext(), null)
        rootView.rv.adapter = mAdapter
        mAdapter?.setOnItemClickListener(object : BaseRecyclerAdapter.OnItemClickListener<String> {
            override fun onItemClick(
                itemView: View,
                position: Int,
                itemViewType: Int,
                item: String
            ) {
                showToast(
                    "position:$position,itemViewType:$itemViewType,data:${
                        mAdapter?.getItem(
                            position
                        )
                    }}"
                )
            }
        })
    }

    override fun initData() {
        mAdapter?.setData(getRandomList())
    }

    override fun getLayoutId(): Int = R.layout.dialog_bottom_sheet1

    /**
     * 其他区域是否变暗(默认变暗)
     */
    override fun isBackgroundDimEnable(): Boolean = true

    /**
     * 首次打开的高度
     */
    override fun getPeekHeight(): Int? {
        return screenHeight / 2
    }

    /**
     * 最大高度,如果和peekHeight相同,就无法向上滑动占满屏幕(默认是Match_Parent)
     */
    override fun getMaxHeight(): Int? {
        return screenHeight / 2
    }
}