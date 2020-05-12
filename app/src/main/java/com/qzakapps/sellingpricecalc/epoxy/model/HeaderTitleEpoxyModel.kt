package com.qzakapps.sellingpricecalc.epoxy.model

import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.qzakapps.sellingpricecalc.R


@EpoxyModelClass(layout = R.layout.header_title_view)
abstract class HeaderTitleEpoxyModel : EpoxyModelWithHolder<HeaderTitleViewHolder>() {

    @EpoxyAttribute lateinit var name: String

    override fun bind(holder: HeaderTitleViewHolder) {
        holder.name.text = name
    }
}

class HeaderTitleViewHolder : EpoxyHolder() {
    lateinit var name: TextView

    override fun bindView(itemView: View) {
        name = itemView.findViewById(R.id.headerTitleTextView)
    }

}