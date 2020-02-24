package com.qzakapps.sellingpricecalc.viewmodels

import androidx.lifecycle.ViewModel
import com.qzakapps.sellingpricecalc.helper.calculateSalePrice
import com.qzakapps.sellingpricecalc.helper.toBigDecimal
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.subjects.BehaviorSubject

interface CalculationViewModelInputs {
    val inputCostPrice: BehaviorSubject<String>
    val inputProfitMargin: BehaviorSubject<String>

}

interface CalculationViewModelOutputs {
    val outputSalePrice: Observable<String>
}

class CalculationViewModel : ViewModel(), CalculationViewModelInputs, CalculationViewModelOutputs {

    override val inputCostPrice: BehaviorSubject<String> = BehaviorSubject.createDefault("")
    override val inputProfitMargin: BehaviorSubject<String> = BehaviorSubject.createDefault("")

    //region inputs
    fun onCostPriceTextChange(text: String){
        inputCostPrice.onNext(text)
    }

    fun onProfitMarginTextChange(text: String){
        inputProfitMargin.onNext(text)
    }

    //endregion

    //region outputs
    override val outputSalePrice: Observable<String> =
            Observable.combineLatest(
                    inputCostPrice,
                    inputProfitMargin,
                    BiFunction {
                        cost, profit ->
                        calculateSalePrice(toBigDecimal(cost), toBigDecimal(profit)).toString()
                    })
    //endregion
}