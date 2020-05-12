package com.qzakapps.sellingpricecalc.epoxy.controller

import android.content.Context
import com.airbnb.epoxy.EpoxyController
import com.qzakapps.sellingpricecalc.R
import com.qzakapps.sellingpricecalc.epoxy.model.costList
import com.qzakapps.sellingpricecalc.epoxy.model.headerTitle
import com.qzakapps.sellingpricecalc.models.Cost

class CostListEpoxyController(val context: Context) : EpoxyController() {

    private var costList: MutableList<Cost> = mutableListOf()

    fun setCostList(costs: List<Cost>) {
        this.costList.run {
            clear()
            addAll(costs)
        }
        requestModelBuild()
    }

    override fun buildModels() {

        headerTitle {
            id("HeaderTitleCostActive")
            name(context.getString(R.string.active_costs))
        }

        for (cost in costList){
            costList {
                id(cost.id)
                name(cost.name)
                cost(cost.cost)
            }
        }

        headerTitle {
            id("HeaderTitleCostActive")
            name(context.getString(R.string.inactive_costs))
        }

    }
}