package com.acorn.basemodule.extendfun

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Base64
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import java.io.*

/**
 * Created by acorn on 2021/6/15.
 */
fun String.base64ToBitmap(): Bitmap {
    val bytes: ByteArray = Base64.decode(this, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

fun Bitmap.toBase64String(): String {
    val outputStream = ByteArrayOutputStream()
    //读取图片到ByteArrayOutputStream
    compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
}

/**
 * 把bitmap作为jpg存入指定路径,需要先获得写入权限
 */
fun Bitmap.bitmap2JpgFile(name: String, filePath: String): File {
    val fileFolder = File(filePath)
    if (!fileFolder.exists()) {
        fileFolder.mkdirs()
    }
    val pathNameSb = StringBuilder()
    pathNameSb.append(filePath)
    if (!filePath.endsWith("/")) {
        pathNameSb.append("/")
    }
    pathNameSb.append(name)
    val file = File(pathNameSb.toString())
    try {
        val out = FileOutputStream(file)
        if (compress(Bitmap.CompressFormat.JPEG, 100, out)) {
            out.flush();
            out.close();
        }
    } catch (e: FileNotFoundException) {
        e.printStackTrace();
    } catch (e: IOException) {
        e.printStackTrace();
    }
    return file
}

fun AppCompatImageView.updateMatrix(matrixCallback: Matrix.(dWidth: Int, dHeight: Int, vWidth: Int, vHeight: Int) -> Unit) {
    //ImageView需要设置ScaleType=Matrix,才能使用此功能
    scaleType = ImageView.ScaleType.MATRIX
    imageMatrix = Matrix().apply {
        //显示图片的宽高
        val dWidth = this@updateMatrix.drawable.intrinsicWidth
        val dHeight = this@updateMatrix.drawable.intrinsicHeight

        //ImageView的宽高
        val vWidth = this@updateMatrix.measuredWidth
        val vHeight = this@updateMatrix.measuredHeight
        this.matrixCallback(dWidth, dHeight, vWidth, vHeight)
    }
}