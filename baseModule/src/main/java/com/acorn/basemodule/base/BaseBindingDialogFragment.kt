package com.acorn.basemodule.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.viewbinding.ViewBinding
import com.acorn.basemodule.network.BaseNetViewModel

/**
 * Created by acorn on 2022/5/27.
 */
abstract class BaseBindingDialogFragment<T: BaseNetViewModel,U : ViewBinding> : BaseDialogFragment<T>() {
    private var _binding: U? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCanceledOnTouchOutside(canceledOnTouchOutside())
        _binding = createBinding(inflater, container)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getLayoutId(): Int {
        //这个方法没用了
        return 0
    }

    abstract fun createBinding(inflater: LayoutInflater, container: ViewGroup?): U
}