package com.qzakapps.sellingpricecalc.epoxy.model

import android.graphics.Typeface
import android.view.Gravity
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
    @EpoxyAttribute var italic: Boolean = false
    @EpoxyAttribute var sizeDp: Float = 24f
    @EpoxyAttribute var center: Boolean = false

    override fun bind(holder: HeaderTitleViewHolder) {
        holder.name.text = name
        if(italic) holder.name.setTypeface(null, Typeface.ITALIC)
        holder.name.textSize = sizeDp
        if(center) holder.name.gravity = Gravity.CENTER else holder.name.gravity = Gravity.NO_GRAVITY
    }
}

class HeaderTitleViewHolder : EpoxyHolder() {
    lateinit var name: TextView

    override fun bindView(itemView: View) {
        name = itemView.findViewById(R.id.headerTitleTextView)
    }

}