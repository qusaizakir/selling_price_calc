package com.qzakapps.sellingpricecalc.helper

import java.math.BigDecimal
import java.math.RoundingMode

val _100 = BigDecimal.valueOf(100.0)
val _1 = BigDecimal.ONE

//region Calculation Formulae

fun calculateSalePriceWithMarkup(markup: BigDecimal, fixedCost: BigDecimal, percentCost: BigDecimal): String {
    return (((markup * fixedCost) / _100) + fixedCost).setTwoDecimalToString()
}

fun calculateSalePriceWithProfit(profit: BigDecimal, fixedCost: BigDecimal, percentCost: BigDecimal): String {
    return (profit + fixedCost).setTwoDecimalToString()
}

fun calculateSalePriceWithProfitMargin(profitMargin: BigDecimal, fixedCost: BigDecimal, percentCost: BigDecimal): String {
    return (fixedCost / (_1 - (profitMargin/ _100))).setTwoDecimalToString()
}

fun calculateMarkupWithSalePrice(salePrice: BigDecimal, fixedCost: BigDecimal, percentCost: BigDecimal): String {
    return (((salePrice - fixedCost) * _100) / fixedCost).setTwoDecimalToString()
}

fun calculateMarkupWithProfit(profit: BigDecimal, fixedCost: BigDecimal, percentCost: BigDecimal): String {
    return ((profit / fixedCost) * _100).setTwoDecimalToString()
}

fun calculateMarkupWithProfitMargin(profitMargin: BigDecimal, fixedCost: BigDecimal, percentCost: BigDecimal): String {
    return (((_1 / (_1 - (profitMargin / _100)))-_1)*_100).setTwoDecimalToString()
}

fun calculateProfitWithSalePrice(salePrice: BigDecimal, fixedCost: BigDecimal, percentCost: BigDecimal): String {
    return (salePrice - fixedCost).setTwoDecimalToString()
}

fun calculateProfitWithMarkup(markup: BigDecimal, fixedCost: BigDecimal, percentCost: BigDecimal): String {
    return ((fixedCost * (_1 + (markup / _100))) - fixedCost).setTwoDecimalToString()
}

fun calculateProfitWithProfitMargin(profitMargin: BigDecimal, fixedCost: BigDecimal, percentCost: BigDecimal): String {
    return ((fixedCost / (_1 - (profitMargin/ _100))) * (profitMargin / _100)).setTwoDecimalToString()
}

fun calculateProfitMarginWithSalePrice(salePrice: BigDecimal, fixedCost: BigDecimal, percentCost: BigDecimal): String {
    return (_1 - (fixedCost / salePrice)).setTwoDecimalToString()
}

fun calculateProfitMarginWithMarkup(markup: BigDecimal, fixedCost: BigDecimal, percentCost: BigDecimal): String {
    return (((fixedCost * (_1 + (markup / _100))) - fixedCost) / (fixedCost * (_1 + (markup / _100))) * _100).setTwoDecimalToString()
}

fun calculateProfitMarginWithProfit(profit: BigDecimal, fixedCost: BigDecimal, percentCost: BigDecimal): String {
    return ((profit / (fixedCost + profit)) * _100).setTwoDecimalToString()
}

//endregion

fun BigDecimal.setTwoDecimalToString(): String {
    return this.setScale(2, RoundingMode.HALF_UP).toString()
}

operator fun BigDecimal.div(other: BigDecimal): BigDecimal {
    return this.divide(other, 2, RoundingMode.HALF_UP)
}