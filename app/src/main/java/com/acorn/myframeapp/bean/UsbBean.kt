package com.acorn.myframeapp.bean

import android.hardware.usb.UsbDevice
import android.os.Parcel
import android.os.Parcelable

/**
 * Created by acorn on 2023/2/3.
 */
data class UsbBean(val device: UsbDevice) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readParcelable(UsbDevice::class.java.classLoader)!!) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(device, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UsbBean> {
        override fun createFromParcel(parcel: Parcel): UsbBean {
            return UsbBean(parcel)
        }

        override fun newArray(size: Int): Array<UsbBean?> {
            return arrayOfNulls(size)
        }
    }
}
