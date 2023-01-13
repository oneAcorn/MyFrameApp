package com.acorn.myframeapp.ui.matrix

import android.graphics.Matrix
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.AppCompatImageView
import com.acorn.myframeapp.R
import com.acorn.myframeapp.demo.BaseDemoActivity
import com.acorn.myframeapp.demo.Demo
import kotlinx.android.synthetic.main.activity_matrix1.*

/**
 * https://medium.com/a-problem-like-maria/understanding-android-matrix-transformations-25e028f56dc7#id_token=eyJhbGciOiJSUzI1NiIsImtpZCI6ImEyOWFiYzE5YmUyN2ZiNDE1MWFhNDMxZTk0ZmEzNjgwYWU0NThkYTUiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJuYmYiOjE2NzM1NzM3NTQsImF1ZCI6IjIxNjI5NjAzNTgzNC1rMWs2cWUwNjBzMnRwMmEyamFtNGxqZGNtczAwc3R0Zy5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsInN1YiI6IjEwMTQ1MjQ2MTQ0NTM3MzYyMzE1MyIsImVtYWlsIjoibGFyZ2VwYWd1bWFsYXJ2YXRhQGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJhenAiOiIyMTYyOTYwMzU4MzQtazFrNnFlMDYwczJ0cDJhMmphbTRsamRjbXMwMHN0dGcuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJuYW1lIjoibGFydmF0YSBQYWd1bWEiLCJwaWN0dXJlIjoiaHR0cHM6Ly9saDMuZ29vZ2xldXNlcmNvbnRlbnQuY29tL2EvQUVkRlRwNXA3M0hJZnJ6NGRIZV9UYlYtYXZFc1dHUTVnam5KRUtYRDJVS2Y9czk2LWMiLCJnaXZlbl9uYW1lIjoibGFydmF0YSIsImZhbWlseV9uYW1lIjoiUGFndW1hIiwiaWF0IjoxNjczNTc0MDU0LCJleHAiOjE2NzM1Nzc2NTQsImp0aSI6ImUwMTQyYjBlZDljMDJkYTFlMWU2NDNiMDEwZmQ3NjVkMTU4OGRhZDIifQ.OAgvtyCUtIcRCiTJlCP24lQCgR3eKFqlWTMyVQkCtoo2JzVY9IUaEmezzjRRlOMz6qKJYJqZQHr55GSGuqfv5vsz9DitVh8BbTrFm2phCmgha8ndEmVM0N_OuPluVVQPu6d5DfEikfLDTOhVF_G52FjjYqfQL-iHpHEFnsfqpZqdsVxqUBXpAVY2hk2TeWawnMyJZYkjSDS0U0rc53Brf9NxG-az-kKCDVzGO-1wCWoOYbZPIDPqzYBey1cv5OCfNFvzRo0Fsnhengc9Twxun7dIZ7jqMR5RgMoGl1ut-2bhOVJ-4aX3-eTOJc1KgTXQnnARR60corB8htguW07bYA
 * We can now translate, scale, rotate and skew images, but what if we want to combine them?
 * The obvious thing might be calling multiple set methods in a row. This, however,
 * will only apply the last transformation — all the previous ones will be overwritten.
 * This is because the set method essentially resets the matrix.
 *
 * Created by acorn on 2023/1/13.
 */
class Matrix1Activity : BaseDemoActivity() {
    companion object {
        private const val CLICK_TRANSLATE_CENTER = 0
        private const val CLICK_SCALE_BY_CENTER = 1
        private const val CLICK_SCALE_NEGATIVE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matrix1)
    }

    override fun initView() {
        super.initView()
        showToolbar("Matrix")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_matrix1, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var isConsume = true
        when (item.itemId) {
            R.id.translate -> {
            }
            else -> {
                isConsume = false
            }
        }
        return if (isConsume) true else super.onOptionsItemSelected(item)
    }

    override fun getItems(): Array<Demo> {
        return arrayOf(
            Demo("translate to center", CLICK_TRANSLATE_CENTER),
            Demo("scale by center", CLICK_SCALE_BY_CENTER),
            Demo("scale negative", CLICK_SCALE_NEGATIVE)
        )
    }

    override fun onItemClick(data: Demo, idOrPosition: Int) {
        when (idOrPosition) {
            CLICK_TRANSLATE_CENTER -> {
                translate2Center()
            }
            CLICK_SCALE_BY_CENTER -> {
                scaleByCenter()
            }
            CLICK_SCALE_NEGATIVE -> {
                scaleNegativeValue()
            }
        }
    }

    private fun AppCompatImageView.updateMatrix(matrixCallback: Matrix.(dWidth: Int, dHeight: Int, vWidth: Int, vHeight: Int) -> Unit) {
        imageMatrix = Matrix().apply {
            //显示图片的宽高
            val dWidth = matrixIv.drawable.intrinsicWidth
            val dHeight = matrixIv.drawable.intrinsicHeight

            //ImageView的宽高
            val vWidth = matrixIv.measuredWidth
            val vHeight = matrixIv.measuredHeight
            this.matrixCallback(dWidth, dHeight, vWidth, vHeight)
        }
    }

    private fun translate2Center() {
        //ImageView需要设置ScaleType=Matrix
        matrixIv.updateMatrix { dWidth, dHeight, vWidth, vHeight ->
            setTranslate(
                (vWidth - dWidth) * 0.5f,
                (vHeight - dHeight) * 0.5f
            )
        }
    }

    private fun scaleByCenter() {
        matrixIv.updateMatrix { dWidth, dHeight, vWidth, vHeight ->
            //缩放的轴心点
            val pivotPointX = dWidth / 2f
            val pivotPointY = dHeight / 2f
            setScale(0.5f, 0.5f, pivotPointX, pivotPointY)
        }
    }

    private fun scaleNegativeValue() {
        matrixIv.updateMatrix { dWidth, dHeight, vWidth, vHeight ->
            //缩放的轴心点
            val pivotPointX = dWidth / 2f
            val pivotPointY = dHeight / 2f
            setScale(1f, -1f, pivotPointX, pivotPointY)
        }
    }
}