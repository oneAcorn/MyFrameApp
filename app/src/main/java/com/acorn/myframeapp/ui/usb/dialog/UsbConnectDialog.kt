package com.acorn.myframeapp.ui.usb.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.acorn.basemodule.base.BaseBindingDialogFragment
import com.acorn.basemodule.network.createSharedViewModel
import com.acorn.myframeapp.bean.UsbBean
import com.acorn.myframeapp.bean.UsbBeanUIState
import com.acorn.myframeapp.databinding.DialogUsbConnectBinding
import com.acorn.myframeapp.ui.usb.adapter.UsbConnectAdapter
import com.acorn.myframeapp.ui.usb.viewmodel.UsbViewModel


/**
 * Created by acorn on 2022/6/28.
 */
class UsbConnectDialog : BaseBindingDialogFragment<UsbViewModel, DialogUsbConnectBinding>() {
    private var mAdapter: UsbConnectAdapter? = null

    companion object {
        fun newInstance(): UsbConnectDialog {
            val args = Bundle()
            val fragment = UsbConnectDialog()
            fragment.arguments = args
            return fragment
        }
    }

    override fun initView(view: View) {
        super.initView(view)
        binding.rv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mAdapter = UsbConnectAdapter(requireContext(), null)
        binding.rv.adapter = mAdapter
    }

    override fun initListener() {
        super.initListener()
        mAdapter?.connectBtnClickCallback = {
        }
        mAdapter?.disconnectBtnClickCallback = {
        }
    }

    override fun initObservers() {
        super.initObservers()
        mViewModel?.usbDevicesLiveData?.observe(this) { state ->
            when (state) {
                is UsbBeanUIState.Init -> {
                    mAdapter?.setData(state.list)
                }
                is UsbBeanUIState.Insert -> {
                    mAdapter?.append(state.bean)
                }
                is UsbBeanUIState.Remove -> {
                    mAdapter?.removeItem(state.bean)
                }
            }
        }
    }

    override fun initData() {
        super.initData()
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogUsbConnectBinding = DialogUsbConnectBinding.inflate(inflater, container, false)

    override fun getViewModel(): UsbViewModel? = createSharedViewModel(UsbViewModel::class.java)
}