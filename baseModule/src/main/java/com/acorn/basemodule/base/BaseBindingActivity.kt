package com.acorn.basemodule.base

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.acorn.basemodule.network.BaseNetViewModel

/**
 * Created by acorn on 2023/1/11.
 */
abstract class BaseBindingActivity<T : BaseNetViewModel, U : ViewBinding> : BaseActivity<T>() {

    protected lateinit var binding: U

    abstract fun createBinding(): U

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = createBinding()
        setContentView(binding.root)
    }
}