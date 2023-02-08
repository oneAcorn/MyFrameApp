package com.acorn.myframeapp.ui.usb.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.acorn.basemodule.base.BaseBindingViewHolder
import com.acorn.basemodule.base.recyclerAdapter.BaseRecyclerAdapter
import com.acorn.basemodule.extendfun.singleClick
import com.acorn.myframeapp.bean.UsbBean
import com.acorn.myframeapp.bean.isConnected
import com.acorn.myframeapp.databinding.ItemUsbDeviceBinding

/**
 * Created by acorn on 2022/6/28.
 */
class UsbConnectAdapter(
    context: Context,
    datas: List<UsbBean>? = null
) :
    BaseRecyclerAdapter<UsbBean, UsbConnectAdapter.BleConnectViewHolder>(context, datas) {
    var connectBtnClickCallback: ((UsbBean) -> Unit)? = null
    var disconnectBtnClickCallback: ((UsbBean) -> Unit)? = null

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BleConnectViewHolder {
        return BleConnectViewHolder(ItemUsbDeviceBinding.inflate(mInflater, parent, false))
    }

    override fun bindData(holder: RecyclerView.ViewHolder, position: Int, item: UsbBean) {
        item ?: return
        (holder as? BleConnectViewHolder)?.bindData(item)
    }

    fun notifyItem(usbBean: UsbBean) {
        val position = data.indexOf(usbBean)
        if (position < 0) return
        notifyItemChanged(position)
    }

    inner class BleConnectViewHolder(binding: ItemUsbDeviceBinding) :
        BaseBindingViewHolder<ItemUsbDeviceBinding>(binding) {

        fun bindData(item: UsbBean) {
            val isConnected: Boolean = item.isConnected()

            binding.deviceNameTv.text = item.device.productName
            binding.connectBtn.visibility = if (isConnected) View.GONE else View.VISIBLE
            binding.disconnectBtn.visibility = if (isConnected) View.VISIBLE else View.GONE
            binding.connectedTv.visibility = if (isConnected) View.VISIBLE else View.GONE
            binding.connectBtn.singleClick {
                connectBtnClickCallback?.invoke(item)
            }
            binding.disconnectBtn.singleClick {
                disconnectBtnClickCallback?.invoke(item)
            }
        }
    }
}