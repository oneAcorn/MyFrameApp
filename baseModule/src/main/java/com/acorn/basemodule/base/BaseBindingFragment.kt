package com.acorn.basemodule.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.acorn.basemodule.R
import com.acorn.basemodule.network.BaseNetViewModel
import kotlinx.android.synthetic.main.base_fragment_layout.view.*

/**
 * Created by acorn on 2023/1/11.
 */
abstract class BaseBindingFragment<T : BaseNetViewModel, U : ViewBinding> : BaseFragment<T>() {
    private var _binding: U? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (isEmbedInBaseLayout()) {
            val rootView = inflater.inflate(R.layout.base_fragment_layout, container, false)
            _binding = createBinding(inflater, rootView.baseContentLayout)
            rootView
        } else {
            _binding = createBinding(inflater, container)
            binding.root
        }
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