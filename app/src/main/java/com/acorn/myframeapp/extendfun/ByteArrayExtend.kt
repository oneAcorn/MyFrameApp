package com.acorn.myframeapp.extendfun

import com.acorn.myframeapp.ui.usb.kits.ByteUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.Charset
import java.util.*

/**
 * Created by acorn on 2023/2/13.
 */

fun main() {
    println("5aa51100".toByteArrayBigOrder().concat("5bb5".toByteArrayBigOrder()).toLogString())
}

private val BYTE_ORDER = ByteOrder.BIG_ENDIAN

/**
 * 16进制字符串转字节数组
 * @return 字节数组
 */
fun String.toByteArr(): ByteArray {
    return ByteUtils.hexToByteArr(this)
}

fun ByteArray.parseToString(charset: Charset = Charset.defaultCharset()): String {
    return String(this, Charset.defaultCharset())
}

fun Short.toByteArr(byteOrder: ByteOrder = BYTE_ORDER): ByteArray {
    return ByteBuffer.allocate(2).order(byteOrder)
        .putShort(this).array()
}

fun Int.toByteArr(): ByteArray {
    return ByteBuffer.allocate(4).order(BYTE_ORDER)
        .putInt(this).array()
}

fun Byte.toHexString(): String {
    return String.format("%02x", this)
}

/**
 * 转换为无符号int
 */
fun Byte.toUnsignedInt(): Int {
    return this.toInt() and 0xff
}

fun ByteArray.toShort(byteOrder: ByteOrder = BYTE_ORDER): Short {
    return ByteBuffer.wrap(this, 0, 2).order(byteOrder).short
}

/**
 * 因为java的int取值范围是-2亿~2亿,而无符号的int取值范围是0~4亿,所以必须转成Long
 */
fun ByteArray.toUnsignedInt(): Long {
    val byteArray = toFixedArrayBigOrder(4)
    return ByteBuffer.wrap(byteArray)
        .order(BYTE_ORDER).int.toLong() and 0xffffffffL
}

/**
 * 转换为无符号Long
 */
fun Byte.toUnsignedLong(): Long {
    return this.toLong() and 0xffL
}

/**
 * 字节数组转16进制字符串
 * @return 16进制字符串
 */
fun ByteArray.toHexString(): String {
    return ByteUtils.byteArrToHex(this)
}

/**
 * @param division 分隔符
 */
fun ByteArray.toLogString(division: String? = ","): String {
    val sb = StringBuilder()
    for (b in this) {
        sb.append(String.format("%02x", b))
        division?.let { sb.append(division) }
    }
    return sb.toString()
}

fun String.toByteArrayBigOrder(): ByteArray = ByteUtils.toByteArrayBigOrder(this)

/**
 * 切割指定区域
 */
fun ByteArray.getByteArraySegment(srcPos: Int, length: Int): ByteArray {
    if (srcPos < 0 || length < 0)
        throw RuntimeException("srcPos($srcPos) must in [0,∞) length($length) must in [0,∞)")
    if (length == 0)
        throw RuntimeException("length can't be zero")
    val byteArrNew = ByteArray(length)
    System.arraycopy(this, srcPos, byteArrNew, 0, length)
    return byteArrNew
}

/**
 * 转换为固定长度的ByteArray，默认前面补0
 * @param fillByte 补位的byte
 */
fun ByteArray.toFixedArrayBigOrder(length: Int, fillByte: Byte = 0x00): ByteArray {
    if (size > length)
        throw RuntimeException("this string'length more than $length")
    val byteArray = ByteArray(length)
    if (fillByte != (0x00).toByte()) {
        for (i in 0 until length - size) {
            byteArray[i] = fillByte
        }
    }
    for ((i, j) in (length - size until length).withIndex()) {
        byteArray[j] = this[i]
    }
    return byteArray
}

fun String.ascii2ByteArray(): ByteArray? {
    val ret = ByteArray(this.length)
    for ((i, c) in this.toCharArray().withIndex()) {
        ret[i] = c.ascii2Byte() ?: return null
    }
    return ret
}


fun ByteArray.concat(secondArr: ByteArray): ByteArray {
    val retArr = this.copyOf(size + secondArr.size)
    System.arraycopy(secondArr, 0, retArr, size, secondArr.size)
    return retArr
}

/**
 * 因为新设备的设备id只包含数字,暂时只管数字.
 * 之后有需要再补充,参考ascii表:
 * https://baike.baidu.com/item/ASCII/309296?fr=kg_general
 */
private fun Char.ascii2Byte(): Byte? {
    return when (this) {
        '0' -> 0x30.toByte()
        '1' -> 0x31.toByte()
        '2' -> 0x32.toByte()
        '3' -> 0x33.toByte()
        '4' -> 0x34.toByte()
        '5' -> 0x35.toByte()
        '6' -> 0x36.toByte()
        '7' -> 0x37.toByte()
        '8' -> 0x38.toByte()
        '9' -> 0x39.toByte()
        else -> null
    }
}