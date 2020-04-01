package com.qzakapps.sellingpricecalc.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.qzakapps.sellingpricecalc.R
import com.qzakapps.sellingpricecalc.models.Cost
import com.qzakapps.sellingpricecalc.models.Template
import kotlinx.android.synthetic.main.calculation_cost_recycler_item.view.*
import kotlinx.android.synthetic.main.calculation_template_recycler_item.view.*

class CalculationLoadTemplateRecyclerAdapter (
    private val dataList: MutableList<Template> = mutableListOf(),
    private val templateClickedInterface: TemplateClicked) :
    RecyclerView.Adapter<CalculationLoadTemplateRecyclerAdapter.TitleViewHolder>() {

    inner class TitleViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener{templateClickedInterface.templateClicked(dataList[adapterPosition])}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.calculation_template_recycler_item, parent,false)
        return TitleViewHolder(view)
    }

    override fun onBindViewHolder(holder: TitleViewHolder, position: Int) {
        dataList[position].run {
            holder.itemView.calculationRecyclerItemTemplateTitle.text = dataList[position].name
        }
    }

    override fun getItemCount() = dataList.size

    fun setData(dataList: List<Template>){
        this.dataList.apply {
            clear()
            addAll(dataList)
        }
        notifyDataSetChanged()
    }

    interface TemplateClicked {
        fun templateClicked(template: Template)
    }
}
