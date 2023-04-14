package com.acorn.myframeapp.ui.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.acorn.basemodule.base.BaseBindingViewHolder
import com.acorn.basemodule.extendfun.logI
import com.acorn.myframeapp.bean.Student
import com.acorn.myframeapp.databinding.ItemStudentBinding

/**
 * Created by acorn on 2023/4/12.
 */
class AsyncDifferAdapter : RecyclerView.Adapter<AsyncDifferAdapter.StudentViewHolder>() {
    private val diffCallback = object : DiffUtil.ItemCallback<Student>() {
        override fun areItemsTheSame(oldItem: Student, newItem: Student): Boolean {
            logI("areItemsTheSame $oldItem,$newItem")
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Student, newItem: Student): Boolean {
            logI("areContentsTheSame $oldItem,$newItem")
            return oldItem.name == newItem.name && oldItem.age == newItem.age
        }
    }
    private val mDiffer = AsyncListDiffer<Student>(this, diffCallback)

    fun submitList(data: List<Student>?) {
        mDiffer.submitList(data)
    }

    fun getReadOnlyData(): List<Student> {
        return mDiffer.currentList
    }

    private fun getMutableData(): MutableList<Student> {
        return getReadOnlyData().toMutableList()
    }

    private fun getItem(position: Int): Student? {
        return mDiffer.currentList.get(position)
    }

    //region 数据操作

    fun setData(list: List<Student>?) {
        submitList(list)
    }

    fun clearData() {
        setData(null)
    }

    fun add(position: Int, item: Student) {
        val list = getReadOnlyData().toMutableList()
        if (position > itemCount) {
            return
        } else if (position == itemCount) {
            list.add(item)
        } else {
            list.add(position, item)
        }
        submitList(list)
    }

    fun prepend(list: List<Student>) {
        val mutableList = getReadOnlyData().toMutableList()
        mutableList.addAll(0, list)
        submitList(mutableList)
    }

    fun append(item: Student) {
        val mutableList = getMutableData()
        mutableList.add(item)
        submitList(mutableList)
    }

    fun append(list: List<Student>) {
        val mutableList = getMutableData()
        mutableList.addAll(list)
        submitList(mutableList)
    }

    fun remove(position: Int) {

        val mutableList = getMutableData()
        mutableList.removeAt(position)
        submitList(mutableList)
    }

    fun replace(position: Int, item: Student?) {
        item ?: return
        if (position >= itemCount) return
        val mutableList = getMutableData()
        mutableList.set(position, item)
        submitList(mutableList)
    }

    fun removeItem(item: Student) {
        val mutableList = getMutableData()
        val position = mutableList.indexOf(item)
        if (position < 0) return
        mutableList.remove(item)
        submitList(mutableList)
    }
    //endregion

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        return StudentViewHolder(
            ItemStudentBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return mDiffer.currentList.size
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }

    inner class StudentViewHolder(binding: ItemStudentBinding) :
        BaseBindingViewHolder<ItemStudentBinding>(binding) {
        fun bindData(item: Student?) {
            item ?: return
            binding.titleTv.text = item.name
            binding.descriptionTv.text = "Age:${item.age}"
        }
    }
}