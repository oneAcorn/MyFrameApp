package com.acorn.basemodule.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * Created by acorn on 2022/10/18.
 */
open class BaseBindingViewHolder<T : ViewBinding>(protected val binding: T) :
    RecyclerView.ViewHolder(binding.root) {
}