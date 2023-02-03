package com.acorn.myframeapp.ui.dialog

import android.widget.Toast
import androidx.core.content.ContextCompat
import com.acorn.basemodule.extendfun.showToast
import com.acorn.basemodule.utils.download.ApkDownloadUtil
import com.acorn.myframeapp.R
import com.acorn.myframeapp.demo.BaseDemoActivity
import com.acorn.myframeapp.demo.BaseNoViewModelDemoActivity
import com.acorn.myframeapp.demo.Demo
import com.qmuiteam.qmui.skin.QMUISkinManager
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet.BottomGridSheetBuilder

/**
 * Created by acorn on 2022/5/27.
 */
class DialogActivity : BaseNoViewModelDemoActivity() {

    companion object {
        private const val CLICK_BOTTOM_SHEET_LIST = 0
        private const val CLICK_BOTTOM_SHEET_GRID = 1
        private const val CLICK_MY_BOTTOM_SHEET = 2
        private const val CLICK_MY_BOTTOM_SHEET2 = 3
        private const val CLICK_MY_BOTTOM_SHEET3 = 4
        private const val CLICK_CONFIRM_DIALOG = 5
        private const val CLICK_UPDATE_DIALOG = 6
    }

    override fun getItems(): Array<Demo> {
        return arrayOf(
            Demo(
                "QMUI_BottomListSheet with drag dismiss",
                CLICK_BOTTOM_SHEET_LIST,
                description = """This dialog needs to declare theme "QMUI.Compat.NoActionBar" in AndroidManifest"""
            ),
            Demo(
                "QMUI_BottomGridSheet",
                CLICK_BOTTOM_SHEET_GRID,
                """This dialog needs to declare theme "QMUI.Compat.NoActionBar" in AndroidManifest"""
            ),
            Demo(
                "MyBottomSheet",
                CLICK_MY_BOTTOM_SHEET,
                "This Dialog extends Google BottomSheetDialogFragment."
            ),
            Demo(
                "MyBottomSheet2 can scroll up",
                CLICK_MY_BOTTOM_SHEET2,
                "This Dialog extends Google BottomSheetDialogFragment."
            ),
            Demo(
                "MyBottomSheet3",
                CLICK_MY_BOTTOM_SHEET3,
                "This Dialog extends Google BottomSheetDialogFragment"
            ),
            Demo(
                "ConfirmDialog",
                CLICK_CONFIRM_DIALOG,
                "This Dialog extends AppCompatDialogFragment"
            ),
            Demo(
                "UpdateDialog",
                CLICK_UPDATE_DIALOG,
                "Download app new version then install it."
            )
        )
    }

    override fun onItemClick(data: Demo, idOrPosition: Int) {
        when (idOrPosition) {
            CLICK_BOTTOM_SHEET_LIST -> {
                showBottomSheetList()
            }
            CLICK_BOTTOM_SHEET_GRID -> {
                showBottomSheetGrid()
            }
            CLICK_MY_BOTTOM_SHEET -> {
                BottomSheetDialog1.newInstance().show(supportFragmentManager, "BottomSheetDialog1")
            }
            CLICK_MY_BOTTOM_SHEET2 -> {
                BottomSheetDialog2.newInstance().show(supportFragmentManager, "BottomSheetDialog2")
            }
            CLICK_MY_BOTTOM_SHEET3 -> {
                BottomSheetDialog3.newInstance().show(supportFragmentManager, "BottomSheetDialog3")
            }
            CLICK_CONFIRM_DIALOG -> {
                ConfirmDialog.newInstance("Title", "内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容",
                    {
                        it.dismiss()
                        showToast("Confirm click")
                    },
                    {
                        showToast("Cancel click")
                    }).show(supportFragmentManager, "ConfirmDialog")
            }
            CLICK_UPDATE_DIALOG -> {
                showUpdateDialog()
            }
            else -> {}
        }

    }

    private fun showBottomSheetList() {
        val builder = QMUIBottomSheet.BottomListSheetBuilder(this)
        builder.setGravityCenter(true)
            .setSkinManager(QMUISkinManager.defaultInstance(this))
            .setTitle("I'm Title")
            .setAddCancelBtn(true)
            .setAllowDrag(true)
            .setNeedRightMark(false) //勾选
            .setOnSheetItemClickListener { dialog, itemView, position, tag ->
                dialog.dismiss()
                Toast.makeText(this, "Item " + (position + 1), Toast.LENGTH_SHORT).show()
            }
//        if (withMark) {
//            builder.setCheckedIndex(40)
//        }
        for (i in 1..99) {
//            if (withIcon) {
            builder.addItem(
                ContextCompat.getDrawable(this, R.drawable.ic_baseline_architecture_24),
                "Item $i"
            )
//            } else {
//                builder.addItem("Item $i")
//            }
        }
        builder.build().show()
    }

    private fun showBottomSheetGrid() {
        val TAG_SHARE_WECHAT_FRIEND = 0
        val TAG_SHARE_WECHAT_MOMENT = 1
        val TAG_SHARE_WEIBO = 2
        val TAG_SHARE_CHAT = 3
        val TAG_SHARE_LOCAL = 4
        val builder = BottomGridSheetBuilder(this)
        builder.addItem(
            R.drawable.ic_baseline_architecture_24,
            "分享到微信",
            TAG_SHARE_WECHAT_FRIEND,
            BottomGridSheetBuilder.FIRST_LINE
        )
            .addItem(
                R.drawable.ic_baseline_anchor_24,
                "分享到朋友圈",
                TAG_SHARE_WECHAT_MOMENT,
                BottomGridSheetBuilder.FIRST_LINE
            )
            .addItem(
                R.drawable.ic_baseline_keyboard_arrow_right_24,
                "分享到微博",
                TAG_SHARE_WEIBO,
                BottomGridSheetBuilder.FIRST_LINE
            )
            .addItem(
                R.drawable.ic_baseline_refresh_24,
                "分享到私信",
                TAG_SHARE_CHAT,
                BottomGridSheetBuilder.FIRST_LINE
            )
            .addItem(
                R.drawable.ic_baseline_network_check_24,
                "保存到本地",
                TAG_SHARE_LOCAL,
                BottomGridSheetBuilder.SECOND_LINE
            )
            .setAddCancelBtn(true)
//            .setSkinManager(QMUISkinManager.defaultInstance(this))
            .setOnSheetItemClickListener { dialog, itemView ->
                dialog.dismiss()
                val tag = itemView.tag as Int
                when (tag) {
                    TAG_SHARE_WECHAT_FRIEND -> Toast.makeText(
                        this,
                        "分享到微信",
                        Toast.LENGTH_SHORT
                    ).show()
                    TAG_SHARE_WECHAT_MOMENT -> Toast.makeText(
                        this,
                        "分享到朋友圈",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    TAG_SHARE_WEIBO -> Toast.makeText(this, "分享到微博", Toast.LENGTH_SHORT)
                        .show()
                    TAG_SHARE_CHAT -> Toast.makeText(this, "分享到私信", Toast.LENGTH_SHORT)
                        .show()
                    TAG_SHARE_LOCAL -> Toast.makeText(this, "保存到本地", Toast.LENGTH_SHORT)
                        .show()
                }
            }.build().show()
    }

    private fun showUpdateDialog() {
        val dialog = UpdateDialog.newInstance(
            "下载新版本",
            "新版本1.0.1",
            true,
            object : UpdateDialog.OnUpdateDialogClickListener {
                override fun onOkClick(dialog: UpdateDialog) { //点击下载
                    ApkDownloadUtil(this@DialogActivity).download("没有url") {
                        dialog.updateDownloadingState(it)
                    }
                }

                override fun onCancelClick(dialog: UpdateDialog) {

                }

            })

        showToast("No apk to download,Pls see source code instead")
//        dialog.show(supportFragmentManager,"UpdateDialog")
    }
}