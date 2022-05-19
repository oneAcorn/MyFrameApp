package com.acorn.basemodule.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.acorn.basemodule.R
import com.acorn.basemodule.dialog.ProgressDialog
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
    protected var viewModel: T? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.base_fragment_layout, container, false)
        inflater.inflate(getLayoutId(), rootView.baseContentLayout)
        return rootView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initParameters()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = createViewModel()
        viewModel?.attachUI(this)
    }

    override fun onResume() {
        super.onResume()
        isPausing = false
        //懒加载,如果在ViewPager中使用,需要用BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        if (mIsFirstVisible) {
            mIsFirstVisible = false
            initView()
            initData()
        }
    }

    override fun onPause() {
        super.onPause()
        isPausing = true
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel?.detachUI()
    }

    abstract fun getLayoutId(): Int

    open fun initParameters() {}

    open fun initView() {}

    open fun initData() {}


    abstract fun createViewModel(): T?

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
        if (!isHidden && progressDialog.dialog?.isShowing != true && !isPausing && !isProgressShowing) {
            progressDialog.show(childFragmentManager, "progressDialog", initDialogMsg())
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
        baseContentLayout.visibility = View.GONE
        baseErrorLayout.visibility = View.VISIBLE
        baseErrorLayout.removeAllViews()
        LayoutInflater.from(requireContext()).inflate(errorLayoutResId(), baseErrorLayout)
        baseErrorLayout.findViewById<View>(R.id.refreshNetBtn)?.singleClick {
            refreshNet()
        }
    }

    override fun showContentLayout() {
        baseContentLayout.visibility = View.VISIBLE
        baseErrorLayout.visibility = View.GONE
    }

    /**
     * 整体展示空布局,特殊情况可重写.
     */
    override fun showNullLayout() {
        baseContentLayout.visibility = View.GONE
        baseErrorLayout.visibility = View.VISIBLE
        baseErrorLayout.removeAllViews()
        LayoutInflater.from(context).inflate(nullLayoutResId(), baseErrorLayout)
    }

    override fun showToast(string: String) {
        Toast.makeText(requireContext(), string, Toast.LENGTH_SHORT).show()
    }
}