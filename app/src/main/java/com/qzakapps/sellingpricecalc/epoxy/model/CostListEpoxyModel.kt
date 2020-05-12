package com.qzakapps.sellingpricecalc.epoxy.model

import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.qzakapps.sellingpricecalc.R


@EpoxyModelClass(layout = R.layout.costlist_cost_recycler_item)
abstract class CostListEpoxyModel : EpoxyModelWithHolder<CostListHolder>() {

    @EpoxyAttribute lateinit var name: String
    @EpoxyAttribute lateinit var cost: String

    override fun bind(holder: CostListHolder) {
        holder.name.text = name
        holder.cost.text = cost
    }
}

class CostListHolder : EpoxyHolder() {
    lateinit var name: TextView
    lateinit var cost: TextView

    override fun bindView(itemView: View) {
        name = itemView.findViewById(R.id.costListNameTextView)
        cost = itemView.findViewById(R.id.costListNumberTextView)
    }

}