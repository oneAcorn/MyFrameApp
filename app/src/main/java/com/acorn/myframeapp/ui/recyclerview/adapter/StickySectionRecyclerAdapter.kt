package com.acorn.myframeapp.ui.recyclerview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import com.acorn.myframeapp.R
import com.acorn.myframeapp.ui.recyclerview.data.SectionHeader
import com.acorn.myframeapp.ui.recyclerview.data.SectionItem
import com.qmuiteam.qmui.widget.section.QMUISection
import com.qmuiteam.qmui.widget.section.QMUIStickySectionAdapter
import kotlinx.android.synthetic.main.item_section_header.view.*

/**
 * Created by acorn on 2022/5/25.
 */
class StickySectionRecyclerAdapter(removeSectionTitleIfOnlyOneSection: Boolean = true) :
    QMUIStickySectionAdapter<SectionHeader, SectionItem, QMUIStickySectionAdapter.ViewHolder>(
        removeSectionTitleIfOnlyOneSection
    ) {
    override fun onCreateSectionHeaderViewHolder(viewGroup: ViewGroup): ViewHolder {
        return SectionHeaderViewHolder(
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_section_header, viewGroup, false)
        )
    }

    override fun onCreateSectionItemViewHolder(viewGroup: ViewGroup): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(viewGroup.context)
                .inflate(android.R.layout.simple_list_item_1, viewGroup, false)
        )
    }

    override fun onCreateSectionLoadingViewHolder(viewGroup: ViewGroup): ViewHolder {
        return ViewHolder( LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.view_footer, viewGroup, false))
    }

    override fun onCreateCustomItemViewHolder(viewGroup: ViewGroup, type: Int): ViewHolder {
        return ViewHolder(View(viewGroup.context))
    }

    override fun onBindSectionHeader(
        holder: ViewHolder?,
        position: Int,
        section: QMUISection<SectionHeader, SectionItem>?
    ) {
        if (holder is SectionHeaderViewHolder) {
            holder.titleTv.text = section?.header?.title
            holder.foldHeader(section?.isFold ?: false)
            holder.arrowIv.setOnClickListener {
                val pos = if (holder.isForStickyHeader) position else holder.getAdapterPosition()
                toggleFold(pos, false)
            }
        }
    }

    override fun onBindSectionItem(
        holder: ViewHolder?,
        position: Int,
        section: QMUISection<SectionHeader, SectionItem>?,
        itemIndex: Int
    ) {
        (holder?.itemView as? TextView)?.text = section?.getItemAt(itemIndex)?.content
    }

    class SectionHeaderViewHolder(itemView: View) : ViewHolder(itemView) {
        val titleTv: TextView
        val arrowIv: AppCompatImageView

        init {
            titleTv = itemView.titleTv
            arrowIv = itemView.arrowIv
        }

        fun foldHeader(isFold: Boolean) {
            arrowIv.rotation = if (isFold) 0f else 90f
        }
    }
}