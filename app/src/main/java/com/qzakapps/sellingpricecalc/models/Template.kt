package com.qzakapps.sellingpricecalc.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Template (
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val costList: List<Cost>,
    val percentageList: List<Percentage>,
    val markup: String,
    val salePrice: String,
    val profit: String,
    val profitMargin: String)