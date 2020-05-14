package com.qzakapps.sellingpricecalc.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

const val UNSAVED_TEMPLATE_ID = "Unsaved"

@Entity
data class Template (
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val singleCost: String,
    val singlePercentage: String,
    val costIdList: List<String>,
    val percentageIdList: List<String>,
    val markup: String,
    val salePrice: String,
    val profit: String,
    val profitMargin: String)