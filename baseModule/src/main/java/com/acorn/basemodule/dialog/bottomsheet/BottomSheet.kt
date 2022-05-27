package com.acorn.basemodule.dialog.bottomsheet

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.acorn.basemodule.R
import com.acorn.basemodule.base.BaseDialog
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheetBehavior
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheetRootLayout
import kotlinx.android.synthetic.main.dialog_bottom_sheet.view.*

/**
 * 参考QMUI->QMUIBottomSheet
 * Created by acorn on 2022/5/26.
 */
class BottomSheet : BaseDialog {
    private lateinit var mRootView: QMUIBottomSheetRootLayout
    private lateinit var mBehavior: QMUIBottomSheetBehavior<QMUIBottomSheetRootLayout>
    private var mAnimateToCancel = false
    private var mAnimateToDismiss = false

    constructor(context: Context) : this(context, R.style.MyBottomSheet)

    constructor(context: Context, themeId: Int) : super(context, themeId) {
        val container = layoutInflater.inflate(R.layout.dialog_bottom_sheet, null)
        mRootView = container.bottomSheetLayout
        mBehavior = QMUIBottomSheetBehavior<QMUIBottomSheetRootLayout>().apply {
            isHideable = mCancelable
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        if (mAnimateToCancel) {
                            // cancel() invoked
                            cancel()
                        } else if (mAnimateToDismiss) {
                            // dismiss() invoked but it it not triggered by cancel()
                            dismiss()
                        } else {
                            // drag to cancel
                            cancel()
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                }

            })
            peekHeight = 0
            setAllowDrag(false)
            skipCollapsed = true
        }
        val rootViewLp = mRootView.layoutParams as CoordinatorLayout.LayoutParams
        rootViewLp.behavior = mBehavior
        container.touchOutSideView.setOnClickListener {
            if (mBehavior.state == BottomSheetBehavior.STATE_SETTLING) {
                return@setOnClickListener
            }
            if (mCancelable && isShowing && shouldWindowCloseOnTouchOutside()) {
                cancel()
            }
        }
        mRootView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                return true
            }
        })
        super.setContentView(
            container, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
    }


}