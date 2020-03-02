package com.qzakapps.sellingpricecalc.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.qzakapps.sellingpricecalc.R
import com.qzakapps.sellingpricecalc.models.CostModel
import kotlinx.android.synthetic.main.calculation_cost_recycler_item.view.*

class CalculationCostRecyclerAdapter (private val dataList: MutableList<CostModel> = mutableListOf()) : RecyclerView.Adapter<CalculationCostRecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.calculation_cost_recycler_item, parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        dataList[position].run {
            holder.itemView.titleTextView.text = title
            holder.itemView.numberTextView.text = cost
        }
    }

    override fun getItemCount() = dataList.size

    fun setData(dataList: List<CostModel>){
        this.dataList.apply {
            clear()
            addAll(dataList)
        }
        notifyDataSetChanged()
    }
}
