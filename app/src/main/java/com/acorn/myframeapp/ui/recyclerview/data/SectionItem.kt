package com.acorn.myframeapp.ui.recyclerview.data

import com.qmuiteam.qmui.widget.section.QMUISection

/**
 * 粘性头部的Item数据
 * Created by acorn on 2022/5/25.
 */
class SectionItem(val content: String?) : QMUISection.Model<SectionItem> {

    override fun cloneForDiff(): SectionItem {
        return SectionItem(content)
    }

    override fun isSameItem(other: SectionItem?): Boolean {
        return content === other?.content || (content?.equals(other?.content) ?: false)
    }

    override fun isSameContent(other: SectionItem?): Boolean {
        return true
    }
}