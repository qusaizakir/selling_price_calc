package com.qzakapps.sellingpricecalc.epoxy.model

import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.qzakapps.sellingpricecalc.R


@EpoxyModelClass(layout = R.layout.empty_content_view)
abstract class EmptyContentEpoxyModel : EpoxyModelWithHolder<EmptyContentViewHolder>() {

    @EpoxyAttribute lateinit var content: String

    override fun bind(holder: EmptyContentViewHolder) {
        holder.content.text = content
    }
}

class EmptyContentViewHolder : EpoxyHolder() {
    lateinit var content: TextView

    override fun bindView(itemView: View) {
        content = itemView.findViewById(R.id.empty_content_view)
    }

}