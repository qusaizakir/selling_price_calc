package com.qzakapps.sellingpricecalc.viewmodels

import androidx.lifecycle.ViewModel
import com.qzakapps.sellingpricecalc.helper.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observable.combineLatest
import io.reactivex.rxjava3.functions.Function3
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.math.BigDecimal

interface CalculationViewModelInputs {
    val inputFixedCost: BehaviorSubject<String>
    val inputPercentCost: BehaviorSubject<String>

    val inputProfitMargin: BehaviorSubject<String>
    val inputSalePrice: BehaviorSubject<String>
    val inputProfit: BehaviorSubject<String>
    val inputMarkup: BehaviorSubject<String>

}

interface CalculationViewModelOutputs {
    fun outputSalePrice(): Observable<String>
    fun outputMarkup(): Observable<String>
    fun outputProfit(): Observable<String>
    fun outputProfitMargin(): Observable<String>
}

class CalculationViewModel : ViewModel(), CalculationViewModelInputs, CalculationViewModelOutputs {

    override val inputFixedCost: BehaviorSubject<String> = BehaviorSubject.createDefault("5")
    override val inputPercentCost: BehaviorSubject<String> = BehaviorSubject.createDefault("")

    override val inputProfitMargin: BehaviorSubject<String> = BehaviorSubject.createDefault("")
    override val inputSalePrice: BehaviorSubject<String> = BehaviorSubject.createDefault("")
    override val inputProfit: BehaviorSubject<String> = BehaviorSubject.createDefault("")
    override val inputMarkup: BehaviorSubject<String> = BehaviorSubject.createDefault("")

    //region inputs
    fun onFixedCostTextChange(text: String){
        inputFixedCost.onNext(text)
    }

    fun onPercentCostTextChange(text: String){
        inputPercentCost.onNext(text)
    }

    fun onProfitMarginTextChange(text: String){
        inputProfitMargin.onNext(text)
    }

    fun onSalePriceTextChange(text: String){
        inputSalePrice.onNext(text)
    }

    fun onProfitTextChange(text: String){
        inputProfit.onNext(text)
    }

    fun onMarkupTextChange(text: String){
        inputMarkup.onNext(text)
    }

    //endregion

    //region outputs
    /**
    Each output will be triggered by user typing on one of the other 3 outputs.
    E.g Sale price will be triggered by either Markup, Profit or Profit Margin being changed.
    */
    override fun outputSalePrice(): Observable<String> {
        return combineLatest(
            inputMarkup,
            inputFixedCost,
            inputPercentCost,
            Function3<String, String, String, String> {
                    markup, fixedCost, percentCost ->
                calculateSalePriceWithMarkup(toBigDecimal(markup), toBigDecimal(fixedCost), toBigDecimal(percentCost))
            }).mergeWith(
            combineLatest(
                inputProfit,
                inputFixedCost,
                inputPercentCost,
                Function3<String, String, String, String> {
                        profit, fixedCost, percentCost ->
                    calculateSalePriceWithProfit(toBigDecimal(profit), toBigDecimal(fixedCost), toBigDecimal(percentCost))
                })
        ).mergeWith(
            combineLatest(
                inputProfitMargin,
                inputFixedCost,
                inputPercentCost,
                Function3<String, String, String, String> {
                        profitMargin, fixedCost, percentCost ->
                    calculateSalePriceWithProfitMargin(toBigDecimal(profitMargin), toBigDecimal(fixedCost), toBigDecimal(percentCost))
                })
        )
    }

    override fun outputMarkup(): Observable<String> {
        return combineLatest(
            inputSalePrice,
            inputFixedCost,
            inputPercentCost,
            Function3<String, String, String, String> {
                    salePrice, fixedCost, percentCost ->
                calculateMarkupWithSalePrice(toBigDecimal(salePrice), toBigDecimal(fixedCost), toBigDecimal(percentCost))
            }).mergeWith(
            combineLatest(
                inputProfit,
                inputFixedCost,
                inputPercentCost,
                Function3<String, String, String, String> {
                        profit, fixedCost, percentCost ->
                    calculateMarkupWithProfit(toBigDecimal(profit), toBigDecimal(fixedCost), toBigDecimal(percentCost))
                })
        ).mergeWith(
            combineLatest(
                inputProfitMargin,
                inputFixedCost,
                inputPercentCost,
                Function3<String, String, String, String> {
                        profitMargin, fixedCost, percentCost ->
                    calculateMarkupWithProfitMargin(toBigDecimal(profitMargin), toBigDecimal(fixedCost), toBigDecimal(percentCost))
                })
        )
    }

    override fun outputProfit(): Observable<String> {
         return combineLatest(
            inputSalePrice,
            inputFixedCost,
            inputPercentCost,
            Function3<String, String, String, String> {
                salePrice, fixedCost, percentCost ->
                    calculateProfitWithSalePrice(toBigDecimal(salePrice), toBigDecimal(fixedCost), toBigDecimal(percentCost))
            }).mergeWith(
             combineLatest(
                 inputMarkup,
                 inputFixedCost,
                 inputPercentCost,
                 Function3<String, String, String, String> {
                         markup, fixedCost, percentCost ->
                     calculateProfitWithMarkup(toBigDecimal(markup), toBigDecimal(fixedCost), toBigDecimal(percentCost))
                 })
            ).mergeWith(
             combineLatest(
                 inputProfitMargin,
                 inputFixedCost,
                 inputPercentCost,
                 Function3<String, String, String, String> {
                         profitMargin, fixedCost, percentCost ->
                     calculateProfitWithProfitMargin(toBigDecimal(profitMargin), toBigDecimal(fixedCost), toBigDecimal(percentCost))
                 })
         )
    }

    override fun outputProfitMargin(): Observable<String> {
        return combineLatest(
            inputSalePrice,
            inputFixedCost,
            inputPercentCost,
            Function3<String, String, String, String> {
                    salePrice, fixedCost, percentCost ->

                    if(toBigDecimal(salePrice) != BigDecimal.ZERO) {
                        calculateProfitMarginWithSalePrice(toBigDecimal(salePrice), toBigDecimal(fixedCost), toBigDecimal(percentCost))
                    }else{
                        "Infinity"
                }
            }).mergeWith(
            combineLatest(
                inputMarkup,
                inputFixedCost,
                inputPercentCost,
                Function3<String, String, String, String> {
                        markup, fixedCost, percentCost ->
                    calculateProfitMarginWithMarkup(toBigDecimal(markup), toBigDecimal(fixedCost), toBigDecimal(percentCost))
                })
        ).mergeWith(
            combineLatest(
                inputProfit,
                inputFixedCost,
                inputPercentCost,
                Function3<String, String, String, String> {
                        profit, fixedCost, percentCost ->
                    calculateProfitMarginWithProfit(toBigDecimal(profit), toBigDecimal(fixedCost), toBigDecimal(percentCost))
                })
        )
    }

    //endregion
}