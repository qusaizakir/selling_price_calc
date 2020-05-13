package com.qzakapps.sellingpricecalc.epoxy.model

import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.qzakapps.sellingpricecalc.R
import com.qzakapps.sellingpricecalc.viewmodels.PercentageClickedCallBack


@EpoxyModelClass(layout = R.layout.percentagelist_cost_recycler_item)
abstract class PercentageListEpoxyModel : EpoxyModelWithHolder<PercentageListHolder>() {

    @EpoxyAttribute lateinit var percentageId: String
    @EpoxyAttribute lateinit var name: String
    @EpoxyAttribute lateinit var percentage: String
    @EpoxyAttribute lateinit var callback: PercentageClickedCallBack

    override fun bind(holder: PercentageListHolder) {
        holder.name.text = name
        holder.percentage.text = percentage
        holder.layout.setOnClickListener { callback.onPercentageClicked(percentageId) }
        holder.layout.setBackgroundColor(Color.TRANSPARENT)
    }
}

class PercentageListHolder : EpoxyHolder() {
    lateinit var name: TextView
    lateinit var percentage: TextView
    lateinit var layout: LinearLayout

    override fun bindView(itemView: View) {
        name = itemView.findViewById(R.id.percentageListNameTextView)
        percentage = itemView.findViewById(R.id.percentageListNumberTextView)
        layout = itemView.findViewById(R.id.percentageListRecyclerviewLL)
    }

}