package com.acorn.myframeapp.bean

/**
 * Created by acorn on 2023/2/7.
 */
sealed class UsbResponseState {
    data class Success(val byteArray: ByteArray?) : UsbResponseState() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Success) return false

            if (byteArray != null) {
                if (other.byteArray == null) return false
                if (!byteArray.contentEquals(other.byteArray)) return false
            } else if (other.byteArray != null) return false

            return true
        }

        override fun hashCode(): Int {
            return byteArray?.contentHashCode() ?: 0
        }
    }

    data class Error(val e: Throwable?) : UsbResponseState()
}