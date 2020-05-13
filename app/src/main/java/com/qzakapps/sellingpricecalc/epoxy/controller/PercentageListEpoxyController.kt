package com.qzakapps.sellingpricecalc.epoxy.controller

import android.content.Context
import com.airbnb.epoxy.EpoxyController
import com.qzakapps.sellingpricecalc.R
import com.qzakapps.sellingpricecalc.epoxy.model.emptyContent
import com.qzakapps.sellingpricecalc.epoxy.model.headerTitle
import com.qzakapps.sellingpricecalc.epoxy.model.percentageList
import com.qzakapps.sellingpricecalc.models.Percentage
import com.qzakapps.sellingpricecalc.viewmodels.PercentageClickedCallBack

class PercentageListEpoxyController(val context: Context, private val percentageCallback: PercentageClickedCallBack) : EpoxyController() {

    private var percentageMutableList: MutableList<Percentage> = mutableListOf()

    fun setPercentageList(percentages: List<Percentage>) {
        this.percentageMutableList.run {
            clear()
            addAll(percentages)
        }
        requestModelBuild()
    }

    override fun buildModels() {

        if(percentageMutableList.isNotEmpty()){

            headerTitle {
                id("CostListTopContent")
                name(context.getString(R.string.instructions_percentage_list))
                italic(false)
                sizeDp(18f)
                center(true)
            }

            headerTitle {
                id("HeaderTitleCostActive")
                name(context.getString(R.string.active_percentages))
                italic(true)
                center(false)
                sizeDp(24f)
            }

            for (percentage in percentageMutableList){
                if(percentage.active){
                    percentageList {
                        id(percentage.id)
                        percentageId(percentage.id)
                        name(percentage.name)
                        percentage(percentage.percentage)
                        callback(percentageCallback)
                    }
                }
            }

            headerTitle {
                id("HeaderTitlePercentageINActive")
                name(context.getString(R.string.inactive_percentages))
            }

            for (percentage in percentageMutableList){
                if(!percentage.active){
                    percentageList {
                        id(percentage.id)
                        percentageId(percentage.id)
                        name(percentage.name)
                        percentage(percentage.percentage)
                        callback(percentageCallback)
                    }
                }
            }
        }else{
            emptyContent {
                id("EmptyContentCostList")
                content(context.getString(R.string.empty_content_percentage_list))
            }
        }



    }
}