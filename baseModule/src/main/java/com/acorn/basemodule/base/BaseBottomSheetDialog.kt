package com.acorn.basemodule.base

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.acorn.basemodule.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * Created by acorn on 2022/5/27.
 */
abstract class BaseBottomSheetDialog : BottomSheetDialogFragment() {
    protected lateinit var rootView: View

    private var bottomSheetBehavior: BottomSheetBehavior<View>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!isBackgroundDimEnable()) {
            //其他区域不变暗
            setStyle(STYLE_NORMAL, R.style.TransBottomSheetDialogStyle)
        }
    }

    /**
     * 用下面这种方式创建View的话,如果使用ViewPager会报错:java.lang.IllegalStateException: Fragment does not have a view
     * 改用onCreateView()中创建View即可.
     */
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val dialog = super.onCreateDialog(savedInstanceState)
//        val view =
//            layoutInflater.inflate(R.layout.dialog_my_bottom_sheet, null)
//        dialog.setContentView(view)
//        initView(view)
//        initData()
//        //设置背景透明,防止圆角被覆盖
//        (view.parent as? View)?.setBackgroundColor(Color.TRANSPARENT)
//        return dialog
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView =
            layoutInflater.inflate(getLayoutId(), null)
                .apply {
                    initView(this)
                    initData()
                }
        return rootView
    }

    override fun onStart() {
        super.onStart()

        //修改默认高度
        dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)?.let {
            //此处拿到的BottomSheetBehavior可以修改BottomSheet的各种行为.
            bottomSheetBehavior = BottomSheetBehavior.from(it)
            getPeekHeight()?.let { peekHeight ->
                //peekHeight是首次打开的高度
                bottomSheetBehavior?.peekHeight = peekHeight
            }

            getMaxHeight()?.let { maxHeight ->
                //设置最大高度
                dialog?.window?.run {
                    setLayout(ViewGroup.LayoutParams.MATCH_PARENT, maxHeight)
                    setGravity(Gravity.BOTTOM)
                }
            }
        }

        getSheetBackgroundColor()?.let {
            //这行代码得在dialog.setcontentView之后调用(onCreateView/onViewCreated中不行)
            (rootView.parent as? View)?.setBackgroundColor(it)
        }
    }

    //region 重写区

    protected open fun isBackgroundDimEnable(): Boolean = true

    protected open fun initView(rootView: View) {}

    protected open fun initData() {}

    /**
     * 首次打开的高度
     */
    protected open fun getPeekHeight(): Int? {
        return null
    }

    protected open fun getMaxHeight(): Int? {
        return null
    }

    protected open fun getSheetBackgroundColor(): Int? = Color.TRANSPARENT

    protected abstract fun getLayoutId(): Int
    //endregion
}