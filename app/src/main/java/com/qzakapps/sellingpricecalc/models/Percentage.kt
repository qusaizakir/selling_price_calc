package com.qzakapps.sellingpricecalc.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

const val SINGLE_PERCENTAGE = "SINGLE_PERC"

@Entity
data class Percentage(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val percentage: String,
    val active: Boolean = false)