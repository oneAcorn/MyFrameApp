package com.acorn.myframeapp.ui.usb.kits

/**
 * Created by acorn on 2022/7/7.
 */
object UsbConstants {
    const val PERMISSION_USB = "com.android.usb.USB_PERMISSION"

    //目前新旧设备都是这个
    const val VENDOR_ID: Int = 0x1fc9

    //目前新旧设备都是这个
    const val PRODUCT_ID: Int = 0x000b

    //读USB返回数据的最大字节
    const val READ_DATA_MAX_SIZE = 512
}