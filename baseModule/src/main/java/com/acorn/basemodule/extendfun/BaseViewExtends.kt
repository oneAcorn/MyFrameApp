package com.acorn.basemodule.extendfun

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Checkable
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.security.MessageDigest
import java.util.*

// 屏幕宽高
val screenWidth get() = Resources.getSystem().displayMetrics.widthPixels
val screenHeight get() = Resources.getSystem().displayMetrics.heightPixels

/**
 * 点击了几次
 */
var <T : View> T.clickTimeQueue: Queue<Long>?
    set(value) = setTag(1766613353, value)
    get() = getTag(1766613353) as? Queue<Long>


/**
 * 检测多次点击
 * @param targetClickTimes 点多少次触发点击事件
 * @param totalMillSec 操作需要在多少秒内完成
 */
inline fun <T : View> T.multiClick(
    targetClickTimes: Short = 7,
    totalMillSec: Long = 2000,
    crossinline block: (T) -> Unit
) {
    setOnClickListener {
        val currentTimeMillis = System.currentTimeMillis()
        if (clickTimeQueue == null) {
            clickTimeQueue = LinkedList<Long>()
        }
        clickTimeQueue?.let { queue ->
            while (queue.size > 0 && currentTimeMillis - queue.peek() > totalMillSec) {
                queue.poll()
            }
            queue.offer(currentTimeMillis)
            if (queue.size >= targetClickTimes) {
                queue.clear()
                block(this)
            }
        }
    }
}
/**
 * View的扩展函数
 * Author: jin
 * Date: 2019/6/13
 * Description:
 */
/**
 * 设置点击监听
 * @param views view对象
 */
fun View.OnClickListener.setOnClickListener(vararg views: View) {
    if (views.isNotEmpty()) {
        views.forEach {
            it.setOnClickListener(this)
        }
    }
}

/**
 * 设置文字颜色
 * @param color 颜色值
 */
fun TextView.setTextColorKt(@ColorRes color: Int) {
    this.setTextColor(ContextCompat.getColor(context, color))
}

/**
 * 添加文字改变监听
 * @param onTextChanged 文字改变
 * @param beforeTextChanged 改变前
 * @param afterTextChanged 改变后
 */
fun TextView.addTextChangedListener(
    onTextChanged: (s: CharSequence?, start: Int, before: Int, count: Int) -> Unit = { _: CharSequence?, _: Int, _: Int, _: Int -> },
    beforeTextChanged: (s: CharSequence?, start: Int, count: Int, after: Int) -> Unit = { _: CharSequence?, _: Int, _: Int, _: Int -> },
    afterTextChanged: (s: Editable?) -> Unit = {}
) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterTextChanged(s)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            beforeTextChanged(s, start, count, after)
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged(s, start, before, count)
        }
    })
}

/**
 * 获取ImageView 中的图片是否为高亮
 */
fun ImageView.isHighlight(): Boolean {
    return this.getBright() > 128
}

/**
 * 获取ImageView中图片的亮值
 */
fun ImageView.getBright(): Int {
//    this.isDrawingCacheEnabled = true
//    var bm: Bitmap? = Bitmap.createBitmap(this.drawingCache)
//    this.isDrawingCacheEnabled = false
    val bm = (drawable as BitmapDrawable).bitmap
    if (bm == null) return -1
    val width = bm.width
    val height = bm.height
    var r: Int
    var g: Int
    var b: Int
    var count = 0
    var bright = 0
    for (i in 0 until width) {
        for (j in 0 until height) {
            count++
            val localTemp = bm.getPixel(i, j)
            r = localTemp or -0xff0001 shr 16 and 0x00ff
            g = localTemp or -0xff01 shr 8 and 0x0000ff
            b = localTemp or -0x100 and 0x0000ff
            bright = (bright.toDouble() + 0.299 * r + 0.587 * g + 0.114 * b).toInt()
        }
    }
//    bm.recycle()
//    bm = null
    return bright / count
}

/**
 * 规定时间内只允许单次点击
 */
inline fun <T : View> T.singleClick(time: Long = 450, crossinline block: (T) -> Unit) {
    setOnClickListener {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - lastClickTime > time || this is Checkable) { //不阻止实现Checkable(如CheckBox)的View连点
            lastClickTime = currentTimeMillis
            block(this)
        }
    }
}

/**
 * 规定时间内只允许单次点击
 * 兼容点击事件设置为this的情况
 */
fun <T : View> T.singleClick(onClickListener: View.OnClickListener, time: Long = 450) {
    setOnClickListener {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - lastClickTime > time || this is Checkable) { //不阻止实现Checkable(如CheckBox)的View连点
            lastClickTime = currentTimeMillis
            onClickListener.onClick(this)
        }
    }
}

var <T : View> T.lastClickTime: Long
    set(value) = setTag(1766613352, value)
    get() = getTag(1766613352) as? Long ?: 0

/**
 * 图片置灰
 * @param saturation 饱和度:0为纯黑白；1为饱和度为100%，即原图
 */
fun ImageView.toGray(saturation: Float = 0.1f) {
    colorFilter = ColorMatrixColorFilter(ColorMatrix().apply {
        setSaturation(saturation)
    })
}

/**
 * String MD5 转换
 */
fun String.md5(): String {
    val bytes = MessageDigest.getInstance("MD5").digest(this.toByteArray())
    return bytes.hex()
}

fun ByteArray.hex(): String {
    return joinToString("") { "%02X".format(it) }
}

fun Bitmap.getRoundedCornerBitmap(roundPx: Float): Bitmap? {
    val output: Bitmap = Bitmap.createBitmap(
        this.width, this
            .height, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(output)
    val color = -0xbdbdbe
    val paint = Paint()
    val rect = Rect(0, 0, this.width, this.height)
    val rectF = RectF(rect)
    paint.isAntiAlias = true
    canvas.drawARGB(0, 0, 0, 0)
    paint.color = color
    canvas.drawRoundRect(rectF, roundPx, roundPx, paint)
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(this, rect, rect, paint)
    return output
}

fun RecyclerView.addOnScrollListener(
    onScrollTopCallBack: (first: Int) -> Unit = {},
    onScrollingCallBack: (first: Int) -> Unit = {},
    onScrollBottomCallBack: () -> Unit = {}
) {
    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        var isSlidingToLast = false
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            val manager = recyclerView.layoutManager as LinearLayoutManager?
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                // 获取当前滚动到的条目位置
                val firstIndex = manager?.findFirstVisibleItemPosition()
                onScrollingCallBack.invoke(firstIndex ?: 0)
                val top = recyclerView.canScrollHorizontally(-1) //-1代表顶部,返回true表示没到顶,还可以滑
                val bottom = recyclerView.canScrollHorizontally(1)  //1代表底部,返回true表示没到底部,还可以滑
                if (!top) {
                    onScrollTopCallBack.invoke(0)
                }
                if (!bottom) {
                    onScrollBottomCallBack.invoke()
                }
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy);
            //dx用来判断横向滑动方向，dy用来判断纵向滑动方向
            isSlidingToLast = dx > 0
        }
    })
}

/**
 * 隐藏软键盘
 */
fun EditText.hideSoftInputFromWindow() {
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        .hideSoftInputFromWindow(
            windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
}