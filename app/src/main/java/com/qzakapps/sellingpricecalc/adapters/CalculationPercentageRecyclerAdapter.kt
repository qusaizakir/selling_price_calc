package com.qzakapps.sellingpricecalc.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.qzakapps.sellingpricecalc.R
import com.qzakapps.sellingpricecalc.models.CostModel
import com.qzakapps.sellingpricecalc.models.PercentageModel
import kotlinx.android.synthetic.main.calculation_cost_recycler_item.view.*

class CalculationPercentageRecyclerAdapter (private val dataList: MutableList<PercentageModel> = mutableListOf()) : RecyclerView.Adapter<CalculationPercentageRecyclerAdapter.TitleNumberViewHolder>() {

    class TitleNumberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleNumberViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.calculation_percentage_recycler_item, parent,false)
        return TitleNumberViewHolder(view)
    }

    override fun onBindViewHolder(holder: TitleNumberViewHolder, position: Int) {
        dataList[position].run {
            holder.itemView.titleTextView.text = title
            holder.itemView.numberTextView.text = cost
        }
    }

    override fun getItemCount() = dataList.size

    fun setData(dataList: List<PercentageModel>){
        this.dataList.apply {
            clear()
            addAll(dataList)
        }
        notifyDataSetChanged()
    }
}
