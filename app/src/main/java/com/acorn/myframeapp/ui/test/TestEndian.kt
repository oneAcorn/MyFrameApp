package com.acorn.myframeapp.ui.test

import com.acorn.basemodule.extendfun.logI
import com.acorn.myframeapp.extendfun.toByteArr
import com.acorn.myframeapp.extendfun.toLogString
import com.acorn.myframeapp.extendfun.toShort
import java.nio.ByteOrder
import kotlin.random.Random

/**
 * Created by acorn on 2024/9/5.
 */
internal object TestEndian {
    @JvmStatic
    fun main(args: Array<String>) {
        asdf()
    }

    private fun asdf() {
//        val a: Short = 155
//        val bytes = getBytes(a)
//        val b = ((0x0000 or bytes[1].toInt()) shl 8 or bytes[0].toInt()).toShort()
//        println("a:$a,b:$b,ashort:${bytes.toShort(ByteOrder.LITTLE_ENDIAN)}")
//        println("bytes:${bytes.toLogString()},bytesB:${getBytes(b).toLogString()}")
//        println(
//            "c:${a.toByteArr().toLogString()},${
//                a.toByteArr(ByteOrder.LITTLE_ENDIAN).toLogString()
//            },${
//                b.toByteArr().toLogString()
//            },${b.toByteArr(ByteOrder.LITTLE_ENDIAN).toLogString()}"
//        )
//        val testA = a.toByteArr(ByteOrder.LITTLE_ENDIAN).toShort(ByteOrder.LITTLE_ENDIAN)
//        val testB = testA.toByteArr()
//        val testC = testB.toShort()
//        println("a:$testA,b:${testB.toLogString()},c:$testC")

        val shorts = shortArrayOf(155, -25856, 3, 188, 24255, -16999)
        val newShorts = PCM16Swapper.get16BitPcm(shorts)
        println("shorts:$shorts,newShorts:$newShorts")
        for (s in shorts) {
            println("ori:$s,0:${toBigEndian0(s)},1:${toBigEndian1(s)}")
        }

        val bytes155 = byteArrayOf(-101, 0)
        val littleShort = bytes155.toShort()
        println("toShort0:${bytes155.toShort()},toShort1:${bytes155.toShort(ByteOrder.LITTLE_ENDIAN)}")
        println(
            "r0:${
                littleShort.toByteArr().toLogString()
            },r1:${littleShort.toByteArr(ByteOrder.LITTLE_ENDIAN).toLogString()}"
        )
        println(
            "0 -> ${
                littleShort.toByteArr().toShort(ByteOrder.LITTLE_ENDIAN)
            },1 -> ${toBigEndian1(littleShort)}"
        )
        println("2 -> $littleShort, 3 -> ${PCM16Swapper.swap(littleShort)}")
    }

    private fun toBigEndian0(s: Short): Short {
        val bytes = getBytes(s)
        return ((0x0000 or bytes[1].toInt()) shl 8 or bytes[0].toInt()).toShort()
    }

    private fun toBigEndian1(s: Short): Short {
        val bytes = s.toByteArr(ByteOrder.LITTLE_ENDIAN)
        return bytes.toShort(ByteOrder.BIG_ENDIAN)
    }

    private fun getBytes(s: Short): ByteArray {
        var s2 = s
        val buf = ByteArray(2)
        for (i in buf.indices) {
            buf[i] = (s2.toInt() and 0x00ff).toByte()
            s2 = (s2.toInt() shr 8).toShort()
        }
        return buf
    }
}
