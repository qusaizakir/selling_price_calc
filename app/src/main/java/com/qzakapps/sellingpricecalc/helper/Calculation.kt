package com.qzakapps.sellingpricecalc.helper

import java.math.BigDecimal
import java.math.RoundingMode

val _100 = BigDecimal.valueOf(100.0)
val _1 = BigDecimal.ONE

fun calculateSalePrice(costPrice: BigDecimal, profitMargin: BigDecimal): BigDecimal {
    return costPrice / (_1 - (profitMargin / _100))
}

fun calculateProfit(salePrice: BigDecimal, profitMargin: BigDecimal): BigDecimal {
    return salePrice*profitMargin
}

fun calculateMarkup(costPrice: BigDecimal, profitMargin: BigDecimal): BigDecimal {
    return profitMargin/costPrice* BigDecimal.valueOf(100)
}


operator fun BigDecimal.div(other: BigDecimal): BigDecimal {
    return this.divide(other, 2, RoundingMode.HALF_UP)
}