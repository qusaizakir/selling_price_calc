package com.qzakapps.sellingpricecalc.helper

import java.math.BigDecimal
import java.text.NumberFormat
import java.text.ParseException

fun toBigDecimal(number: String): BigDecimal {
    return try {
        NumberFormat.getInstance().parse(number)?.toDouble()?.let { BigDecimal(it) } ?: BigDecimal.TEN
    } catch (e : ParseException){
        return BigDecimal.TEN
    }
}
