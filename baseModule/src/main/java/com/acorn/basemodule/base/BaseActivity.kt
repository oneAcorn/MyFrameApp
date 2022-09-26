package com.acorn.basemodule.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import com.acorn.basemodule.R
import com.acorn.basemodule.dialog.ProgressDialog
import com.acorn.basemodule.extendfun.showToast
import com.acorn.basemodule.extendfun.singleClick
import com.acorn.basemodule.network.BaseNetViewModel
import com.acorn.basemodule.network.INetworkUI
import com.acorn.basemodule.network.MyException
import com.acorn.basemodule.utils.CommonCaches
import com.acorn.basemodule.utils.InternationalContextWrapper
import kotlinx.android.synthetic.main.base_activity_layout.*
import kotlinx.android.synthetic.main.common_layout_title.view.*


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
    private var toolbar: Toolbar? = null
    private lateinit var centerTitleTv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = getViewModel()
        mViewModel?.attachUI(this)
//        QMUIStatusBarHelper.translucent(this)
    }

    override fun attachBaseContext(newBase: Context) {
        //see https://stackoverflow.com/questions/40221711/android-context-getresources-updateconfiguration-deprecated
        //动态切换语言(国际化)
        super.attachBaseContext(
            InternationalContextWrapper.wrap(
                newBase,
                CommonCaches.currentLanguage ?: ""
            )
        )

        //国际化在Material库中可能出现的bug,还没出现,暂时注释
        /**
         *  After android Material library implementation of ContextThemeWrapper to
         *  support dark mode, the language setting would break and language setting is lost.
         *  After months of head scratching, problem was resolved by adding the following code
         *  to Activity and Fragment onCreate method
         */
//        var context: Context? =
//            MyContextWrapper.wrap(this /*in fragment use getContext() instead of this*/, "fr")
//        resources.updateConfiguration(
//            context!!.resources.configuration,
//            context!!.resources.displayMetrics
//        )
    }

    override fun setContentView(layoutResID: Int) {
        if (isEmbedInBaseLayout()) {
            super.setContentView(R.layout.base_activity_layout)
            baseContentLayout.removeAllViews()
            baseContentLayout.addView(LayoutInflater.from(this).inflate(layoutResID, null))
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
//        ImmersionBar.with(this)
//            .statusBarColor(R.color.colorPrimary)
//            .init()
//        ImmersionBar.setTitleBarMarginTop(this, topView)
        initParameters()
        initView()
        initListener()
        initData()
    }

    override fun onResume() {
        super.onResume()
        isPausing = false
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

    open fun initListener() {}

    /**
     * 是否把布局嵌入到基础布局中
     */
    open fun isEmbedInBaseLayout(): Boolean = true

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

    protected fun showToolbar(
        centerTitle: String? = null,
        callbackBefore: ((Toolbar) -> Unit)? = null,
        callbackAfter: ((Toolbar) -> Unit)? = null
    ) {
        if (!isEmbedInBaseLayout()) {
            throw MyException("Need embed in baseLayout")
        }
        val viewStub: View? = findViewById(R.id.titleViewStub)
        (viewStub as? ViewStub)?.inflate()
        var isFirstInit = false
        if (toolbar == null) {
            toolbar = findViewById<Toolbar>(R.id.toolbar)
            centerTitleTv = findViewById(R.id.centerTitleTv)
            isFirstInit = true
        }
        callbackBefore?.invoke(toolbar!!)
        if (isFirstInit) {
            setSupportActionBar(toolbar)
            toolbar?.setNavigationOnClickListener {
                onBackPressed()
            }
        }
        callbackAfter?.invoke(toolbar!!)
        if (centerTitle != null) {
            //隐藏默认标题
            supportActionBar?.title = null
            centerTitleTv.visibility = View.VISIBLE
            centerTitleTv.text = centerTitle
        } else {
            centerTitleTv.visibility = View.GONE
        }
    }

    protected fun setLeftIv(callback: (AppCompatImageView.() -> Unit)) {
        if (toolbar == null)
            return
        callback.invoke(toolbar!!.leftIv)
    }

    protected fun setToolbar(callback: (Toolbar.() -> Unit)) {
        if (toolbar == null)
            return
        callback.invoke(toolbar!!)
    }

    override fun showProgressDialog(msg: String?, cancelable: Boolean?) {
        if (progressDialog.dialog?.isShowing != true && !isPausing && !isProgressShowing) {
            val showMsg =
                if (msg?.isNotEmpty() == true) {
                    msg
                } else {
                    getString(R.string.loading_wait)
                }
            progressDialog.backPressCancelable = cancelable ?: true
            progressDialog.show(supportFragmentManager, "progressDialog", showMsg)
            isProgressShowing = true
        }
    }

    override fun showProgressDialog(msgRes: Int, vararg params: Any?, cancelable: Boolean?) {
        val msg = if (params.isEmpty()) {
            getString(msgRes)
        } else {
            getString(msgRes, params)
        }
        showProgressDialog(msg, cancelable)
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

    override fun showTip(string: String) {
        showToast(string)
    }

    override fun showTip(msgRes: Int, vararg params: Any?) {
        val msg = if (params.isEmpty()) {
            getString(msgRes)
        } else {
            getString(msgRes, *params)
        }
        showTip(msg)
    }
}