package com.acorn.basemodule.base

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.acorn.basemodule.R
import com.acorn.basemodule.dialog.ProgressDialog
import com.acorn.basemodule.extendfun.showToast
import com.acorn.basemodule.extendfun.singleClick
import com.acorn.basemodule.network.BaseNetViewModel
import com.acorn.basemodule.network.INetworkUI
import kotlinx.android.synthetic.main.base_fragment_layout.*
import kotlinx.android.synthetic.main.base_fragment_layout.view.*

/**
 * Created by acorn on 2022/5/18.
 */
abstract class BaseFragment<T : BaseNetViewModel> : Fragment(), INetworkUI {
    private var mIsFirstVisible = true
    private var isProgressShowing = false
    private var isPausing = false
    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog.newInstance {
            isProgressShowing = false
        }
    }
    protected var mViewModel: T? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (isEmbedInBaseLayout()) {
            val rootView = inflater.inflate(R.layout.base_fragment_layout, container, false)
            inflater.inflate(getLayoutId(), rootView.baseContentLayout)
            rootView
        } else {
            inflater.inflate(getLayoutId(), container, false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initParameters()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = getViewModel()
        mViewModel?.attachUI(this)
    }

    override fun onResume() {
        super.onResume()
        isPausing = false
        //懒加载,如果在ViewPager中使用,需要用BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        if (mIsFirstVisible) {
            mIsFirstVisible = false
            initView()
            initListener()
            initData()
        }
    }

    override fun onPause() {
        super.onPause()
        isPausing = true
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel?.detachUI()
    }

    abstract fun getLayoutId(): Int

    open fun initParameters() {}

    open fun initView() {}

    open fun initData() {}

    open fun initListener() {}

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

    override fun showProgressDialog(msg: String?, cancelable: Boolean?) {
        if (!isHidden && progressDialog.dialog?.isShowing != true && !isPausing && !isProgressShowing) {
            val showMsg =
                if (msg?.isNotEmpty() == true) {
                    msg
                } else {
                    getString(R.string.loading_wait)
                }
            progressDialog.backPressCancelable = cancelable ?: true
            progressDialog.show(childFragmentManager, "progressDialog", showMsg)
            isProgressShowing = true
        }
    }

    override fun showProgressDialog(msgRes: Int, vararg params: Any?, cancelable: Boolean?) {
        val msg = if (params.isEmpty()) {
            getString(msgRes)
        } else {
            getString(msgRes, params)
        }
        showProgressDialog(msg)
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
        LayoutInflater.from(requireContext()).inflate(errorLayoutResId(), baseErrorLayout)
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
        LayoutInflater.from(context).inflate(nullLayoutResId(), baseErrorLayout)
    }

    override fun showTip(string: String) {
        showToast(string)
    }

    override fun showTip(msgRes: Int, vararg params: Any?) {
        val msg = if (params.isEmpty()) {
            getString(msgRes)
        } else {
            getString(msgRes, params)
        }
        showTip(msg)
    }
}