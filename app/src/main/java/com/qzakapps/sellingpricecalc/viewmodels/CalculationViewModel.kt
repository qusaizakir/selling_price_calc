package com.qzakapps.sellingpricecalc.viewmodels

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

interface CalculationViewModelInputs {
    val inputCostPrice: BehaviorSubject<String>
    val inputProfitMargin: BehaviorSubject<String>

}

interface CalculationViewModelOutputs {
    val outputSalePrice: Observable<String>
}

class CalculationViewModel : ViewModel(), CalculationViewModelInputs, CalculationViewModelOutputs {

    var inputs: CalculationViewModelInputs = this
    var outputs: CalculationViewModelOutputs = this

    override val inputCostPrice = BehaviorSubject.createDefault("")
    override val inputProfitMargin = BehaviorSubject.createDefault("")

    fun onCostPriceTextChange(text: String){
        inputCostPrice.onNext(text)
    }

    fun onProfitMarginTextChange(text: String){
        inputProfitMargin.onNext(text)
    }

    override val outputSalePrice: Observable<String> = inputCostPrice
}