package com.qzakapps.sellingpricecalc.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

const val SINGLE_COST = "SINGLE_COST"

@Entity
data class Cost(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val cost: String)