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
import com.qzakapps.sellingpricecalc.viewmodels.CostClickedCallBack


@EpoxyModelClass(layout = R.layout.costlist_cost_recycler_item)
abstract class CostListEpoxyModel : EpoxyModelWithHolder<CostListHolder>() {

    @EpoxyAttribute lateinit var costId: String
    @EpoxyAttribute lateinit var name: String
    @EpoxyAttribute lateinit var cost: String
    @EpoxyAttribute lateinit var callback: CostClickedCallBack

    override fun bind(holder: CostListHolder) {
        holder.name.text = name
        holder.cost.text = cost
        holder.layout.setOnClickListener { callback.onCostClicked(costId) }
        holder.layout.setBackgroundColor(Color.TRANSPARENT)
    }
}

class CostListHolder : EpoxyHolder() {
    lateinit var name: TextView
    lateinit var cost: TextView
    lateinit var layout: LinearLayout

    override fun bindView(itemView: View) {
        name = itemView.findViewById(R.id.costListNameTextView)
        cost = itemView.findViewById(R.id.costListNumberTextView)
        layout = itemView.findViewById(R.id.costListRecyclerviewLL)
    }

}