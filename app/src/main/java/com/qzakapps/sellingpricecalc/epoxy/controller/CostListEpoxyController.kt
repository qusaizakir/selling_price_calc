package com.qzakapps.sellingpricecalc.epoxy.controller

import android.content.Context
import com.airbnb.epoxy.EpoxyController
import com.qzakapps.sellingpricecalc.R
import com.qzakapps.sellingpricecalc.epoxy.model.costList
import com.qzakapps.sellingpricecalc.epoxy.model.emptyContent
import com.qzakapps.sellingpricecalc.epoxy.model.headerTitle
import com.qzakapps.sellingpricecalc.models.Cost
import com.qzakapps.sellingpricecalc.viewmodels.CostClickedCallBack

class CostListEpoxyController(val context: Context, private val costCallback: CostClickedCallBack) : EpoxyController() {

    private var costMutableList: MutableList<Cost> = mutableListOf()

    fun setCostList(costs: List<Cost>) {
        this.costMutableList.run {
            clear()
            addAll(costs)
        }
        requestModelBuild()
    }

    override fun buildModels() {

        if(costMutableList.isNotEmpty()){

            headerTitle {
                id("CostListTopContent")
                name(context.getString(R.string.instructions_cost_list))
                italic(false)
                sizeDp(18f)
                center(true)
            }

            headerTitle {
                id("HeaderTitleCostActive")
                name(context.getString(R.string.active_costs))
                italic(true)
                center(false)
                sizeDp(24f)
            }

            for (cost in costMutableList){
                if(cost.active){
                    costList {
                        id(cost.id)
                        costId(cost.id)
                        name(cost.name)
                        cost(cost.cost)
                        callback(costCallback)
                    }
                }
            }

            headerTitle {
                id("HeaderTitleCostINActive")
                name(context.getString(R.string.inactive_costs))
                italic(true)
                center(false)
                sizeDp(24f)
            }

            for (cost in costMutableList){
                if(!cost.active){
                    costList {
                        id(cost.id)
                        costId(cost.id)
                        name(cost.name)
                        cost(cost.cost)
                        callback(costCallback)
                    }
                }
            }
        }else{

            emptyContent {
                id("EmptyContentCostList")
                content(context.getString(R.string.empty_content_cost_list))
            }

        }



    }
}