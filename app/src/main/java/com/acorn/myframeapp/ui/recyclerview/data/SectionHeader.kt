package com.acorn.myframeapp.ui.recyclerview.data

import com.qmuiteam.qmui.widget.section.QMUISection

/**
 * 粘性头部的头部数据
 * Created by acorn on 2022/5/25.
 */
class SectionHeader(val title: String?) : QMUISection.Model<SectionHeader> {

    override fun cloneForDiff(): SectionHeader {
        return SectionHeader(title)
    }

    override fun isSameItem(other: SectionHeader?): Boolean {
        return title === other?.title || (title?.equals(other?.title) ?: false)
    }

    override fun isSameContent(other: SectionHeader?): Boolean {
        return true
    }
}