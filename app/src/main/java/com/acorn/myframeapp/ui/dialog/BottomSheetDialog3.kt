package com.acorn.myframeapp.ui.dialog

import android.os.Bundle
import com.acorn.basemodule.base.BaseBottomSheetDialog
import com.acorn.myframeapp.R

/**
 * Created by acorn on 2022/5/27.
 */
class BottomSheetDialog3 : BaseBottomSheetDialog() {

    companion object {
        fun newInstance(): BottomSheetDialog3 {
            val args = Bundle()

            val fragment = BottomSheetDialog3()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getLayoutId(): Int = R.layout.dialog_bottom_sheet2

    override fun isBackgroundDimEnable(): Boolean = false
}