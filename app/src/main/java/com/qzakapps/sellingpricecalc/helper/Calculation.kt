package com.qzakapps.sellingpricecalc.helper

import java.math.BigDecimal
import java.math.RoundingMode

val _100 = BigDecimal.valueOf(100.0)
val _1 = BigDecimal.ONE

//region Calculation Formulae

fun calculateSalePrice(costPrice: BigDecimal, profitMargin: BigDecimal): String {
    return (costPrice / (_1 - (profitMargin / _100))).setTwoDecimalToString()
}

fun calculateProfit(salePrice: BigDecimal, profitMargin: BigDecimal): String {
    return (salePrice*(profitMargin/_100)).setTwoDecimalToString()
}

fun calculateProfitMargin(profit: BigDecimal, salePrice: BigDecimal): String {
    return ((profit/ salePrice) * _100).setTwoDecimalToString()
}

fun calculateMarkup(profit: BigDecimal, costPrice: BigDecimal): String {
    return  ((profit / costPrice) * _100).setTwoDecimalToString()
}





fun calculateSalePriceWithMarkup(markup: BigDecimal, fixedCost: BigDecimal, percentCost: BigDecimal): String {
    return (((markup * fixedCost) / _100) + fixedCost).setTwoDecimalToString()
}

fun calculateSalePriceWithProfit(profit: BigDecimal, fixedCost: BigDecimal, percentCost: BigDecimal): String {
    return (profit + fixedCost).setTwoDecimalToString()
}

fun calculateSalePriceWithProfitMargin(profitMargin: BigDecimal, fixedCost: BigDecimal, percentCost: BigDecimal): String {
    return (fixedCost / (_1 - profitMargin)).setTwoDecimalToString()
}


fun calculateMarkupWithSalePrice(salePrice: BigDecimal, fixedCost: BigDecimal, percentCost: BigDecimal): String {
    return (((salePrice - fixedCost) * _100) / fixedCost).setTwoDecimalToString()
}

fun calculateMarkupWithProfit(profit: BigDecimal, fixedCost: BigDecimal, percentCost: BigDecimal): String {
    return ((profit / fixedCost) * _100).setTwoDecimalToString()
}

fun calculateMarkupWithProfitMargin(profitMargin: BigDecimal, fixedCost: BigDecimal, percentCost: BigDecimal): String {
    return
}



//endregion

fun BigDecimal.setTwoDecimalToString(): String {
    return this.setScale(2, RoundingMode.HALF_UP).toString()
}

operator fun BigDecimal.div(other: BigDecimal): BigDecimal {
    return this.divide(other, 2, RoundingMode.HALF_UP)
}