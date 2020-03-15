package com.qzakapps.sellingpricecalc.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Percentage(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val cost: String)