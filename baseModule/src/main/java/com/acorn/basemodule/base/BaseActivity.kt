package com.acorn.basemodule.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.acorn.basemodule.R
import com.acorn.basemodule.dialog.ProgressDialog
import com.acorn.basemodule.extendfun.showToast
import com.acorn.basemodule.extendfun.singleClick
import com.acorn.basemodule.network.BaseNetViewModel
import com.acorn.basemodule.network.INetworkUI
import com.acorn.basemodule.network.MyException
import com.acorn.basemodule.utils.Caches
import com.acorn.basemodule.utils.MyContextWrapper
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
        super.attachBaseContext(MyContextWrapper.wrap(newBase, Caches.currentLanguage ?: ""))

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

    /**
     * ???????????????????????????????????????
     */
    open fun isEmbedInBaseLayout(): Boolean = true

    abstract fun getViewModel(): T?

    /**
     * ??????????????????
     */
    open fun errorLayoutResId(): Int = R.layout.common_net_fail_view

    open fun nullLayoutResId(): Int = R.layout.common_empty_view

    /**
     * ??????????????????
     */
    open fun refreshNet() {

    }

    protected fun showToolbar(
        centerTitle: String? = null,
        callback: ((Toolbar) -> Unit)? = null
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
        callback?.invoke(toolbar!!)
        if (isFirstInit) {
            setSupportActionBar(toolbar)
            toolbar?.setNavigationOnClickListener {
                onBackPressed()
            }
        }
        if (centerTitle != null) {
            //??????????????????
            supportActionBar?.title = null
            centerTitleTv.visibility = View.VISIBLE
            centerTitleTv.text = centerTitle
        } else {
            centerTitleTv.visibility = View.GONE
        }
    }

//    /**
//     * @param title ??????
//     * @param isShowBackIcon ????????????????????????
//     * @param rightText ??????????????????
//     * @param rightTextColor ?????????????????????
//     * @param rightTextClickListener ????????????????????????
//     */
//    protected fun showTitleLayout(
//        title: String,
//        isShowBackIcon: Boolean = true,
//        rightText: String? = null,
//        rightTextColor: Int? = null,
//        rightTextClickListener: (() -> Unit)? = null
//    ) {
//        val viewStub: View? = findViewById(R.id.titleViewStub)
//        (viewStub as? ViewStub)?.inflate()
//        findViewById<View>(R.id.baseBackIv)
//            .singleClick {
//                onBackPressed()
//            }
//        findViewById<TextView>(R.id.baseTitleTv).text = title
//
//        baseBackIv.visibility = if (isShowBackIcon) View.VISIBLE else View.GONE
//        rightTv.visibility = if (rightText == null) View.GONE else View.VISIBLE
//
////        val titleLayout = findViewById<View>(R.id.baseTitleLayout)
////        ImmersionBar.with(this).titleBar(viewStub).init()
//
//        rightText?.let {
//            rightTv.visibility = View.VISIBLE
//            rightTv.text = it
//            rightTextColor?.let { color ->
//                rightTv.setTextColor(color)
//            }
//            rightTv.singleClick {
//                rightTextClickListener?.invoke()
//            }
//        }
//    }

    override fun showProgressDialog() {
        if (progressDialog.dialog?.isShowing != true && !isPausing && !isProgressShowing) {
            progressDialog.show(supportFragmentManager, "progressDialog", initDialogMsg())
            isProgressShowing = true
        }
    }

    override fun dismissProgressDialog() {
        //????????????progressDialog.dialog?.isShowing??????????????????show??????????????????????????????,
        // Fragment??????????????????????????????getDialog()??????
        if (isProgressShowing) {
            progressDialog.dismiss()
            isProgressShowing = false
        }
    }

    /**
     *  progressDialog ????????????
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
     * ?????????????????????,?????????????????????.
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

}