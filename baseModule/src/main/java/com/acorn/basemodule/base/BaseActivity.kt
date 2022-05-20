package com.acorn.basemodule.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.acorn.basemodule.R
import com.acorn.basemodule.dialog.ProgressDialog
import com.acorn.basemodule.extendfun.singleClick
import com.acorn.basemodule.network.BaseNetViewModel
import com.acorn.basemodule.network.INetworkUI
import kotlinx.android.synthetic.main.base_activity_layout.*

/**
 * Created by acorn on 2022/5/19.
 */
abstract class BaseActivity<T : BaseNetViewModel> : AppCompatActivity(), INetworkUI {
    private var isProgressShowing = false
    private var isPausing = false
    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog.newInstance {
            isProgressShowing = false
        }
    }
    protected var mViewModel: T? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = getViewModel()
        mViewModel?.attachUI(this)
    }

    override fun setContentView(layoutResID: Int) {
        if (isEmbedInBaseLayout()) {
            setContentView(LayoutInflater.from(this).inflate(layoutResID, null))
        } else {
            super.setContentView(layoutResID)
        }
        init()
    }

    override fun setContentView(view: View?) {
        if (isEmbedInBaseLayout()) {
            super.setContentView(R.layout.base_activity_layout)
            baseContentLayout.removeAllViews()
            baseContentLayout.addView(view)
        } else {
            super.setContentView(view)
        }
        init()
    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        super.setContentView(view, params)
        if (isEmbedInBaseLayout()) {
            super.setContentView(R.layout.base_activity_layout)
            baseContentLayout.removeAllViews()
            baseContentLayout.addView(view, params)
        } else {
            super.setContentView(view, params)
        }
        init()
    }

    private fun init() {
        initParameters()
        initView()
        initData()
    }

    override fun onPause() {
        super.onPause()
        isPausing = true
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel?.detachUI()
    }

    open fun initParameters() {}

    open fun initView() {}

    open fun initData() {}

    /**
     * 是否把布局嵌入到基础布局中
     */
    open fun isEmbedInBaseLayout(): Boolean = false

    abstract fun getViewModel(): T?

    /**
     * 网络错误布局
     */
    open fun errorLayoutResId(): Int = R.layout.common_net_fail_view

    open fun nullLayoutResId(): Int = R.layout.common_empty_view

    /**
     * 重新加载网络
     */
    open fun refreshNet() {

    }

    override fun showProgressDialog() {
        if (progressDialog.dialog?.isShowing != true && !isPausing && !isProgressShowing) {
            progressDialog.show(supportFragmentManager, "progressDialog", initDialogMsg())
            isProgressShowing = true
        }
    }

    override fun dismissProgressDialog() {
        //不能使用progressDialog.dialog?.isShowing，因为在刚刚show之后的瞬间调用此方法,
        // Fragment还没有初始化完成导致getDialog()为空
        if (isProgressShowing) {
            progressDialog.dismiss()
            isProgressShowing = false
        }
    }

    /**
     *  progressDialog 提示信息
     */
    open fun initDialogMsg(): String {
        return getString(R.string.loading_wait)
    }

    override fun showErrorLayout() {
        if (!isEmbedInBaseLayout())
            return
        baseContentLayout.visibility = View.GONE
        baseErrorLayout.visibility = View.VISIBLE
        baseErrorLayout.removeAllViews()
        LayoutInflater.from(this).inflate(errorLayoutResId(), baseErrorLayout)
        baseErrorLayout.findViewById<View>(R.id.refreshNetBtn)?.singleClick {
            refreshNet()
        }
    }

    override fun showContentLayout() {
        if (!isEmbedInBaseLayout())
            return
        baseContentLayout.visibility = View.VISIBLE
        baseErrorLayout.visibility = View.GONE
    }

    /**
     * 整体展示空布局,特殊情况可重写.
     */
    override fun showNullLayout() {
        if (!isEmbedInBaseLayout())
            return
        baseContentLayout.visibility = View.GONE
        baseErrorLayout.visibility = View.VISIBLE
        baseErrorLayout.removeAllViews()
        LayoutInflater.from(this).inflate(nullLayoutResId(), baseErrorLayout)
    }

    override fun showToast(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }
}