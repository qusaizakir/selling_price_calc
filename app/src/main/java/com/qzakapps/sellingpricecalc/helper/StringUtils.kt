package com.qzakapps.sellingpricecalc.helper

import com.google.android.material.textfield.TextInputEditText
import java.math.BigDecimal
import java.text.NumberFormat
import java.text.ParseException

fun toBigDecimal(number: String): BigDecimal {
    return try {
        NumberFormat.getInstance().parse(number)?.toDouble()?.let { BigDecimal(it) } ?: BigDecimal.ZERO
    } catch (e : ParseException){
        return BigDecimal.ZERO
    }
}

fun TextInputEditText.clearText() {
    this.setText("")
}
