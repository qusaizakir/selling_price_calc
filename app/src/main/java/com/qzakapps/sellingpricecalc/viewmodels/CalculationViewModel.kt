package com.qzakapps.sellingpricecalc.viewmodels

import androidx.lifecycle.ViewModel
import com.qzakapps.sellingpricecalc.helper.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observable.combineLatest
import io.reactivex.rxjava3.functions.Function3
import io.reactivex.rxjava3.subjects.BehaviorSubject

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

    override val inputFixedCost: BehaviorSubject<String> = BehaviorSubject.createDefault("")
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
                    "using profitMargin"
                })
        )
    }

    override fun outputProfit(): Observable<String> {
         return combineLatest(
            inputSalePrice,
            inputFixedCost,
            inputPercentCost,
            Function3<String, String, String, String> {
                salePrice, fixedCost, percentCost -> "using sale price"
            }).mergeWith(
             combineLatest(
                 inputMarkup,
                 inputFixedCost,
                 inputPercentCost,
                 Function3<String, String, String, String> {
                         markup, fixedCost, percentCost -> "using markup"
                 })
            ).mergeWith(
             combineLatest(
                 inputProfitMargin,
                 inputFixedCost,
                 inputPercentCost,
                 Function3<String, String, String, String> {
                         profitMargin, fixedCost, percentCost -> "using profitMargin"
                 })
         )
    }

    override fun outputProfitMargin(): Observable<String> {
        return combineLatest(
            inputSalePrice,
            inputFixedCost,
            inputPercentCost,
            Function3<String, String, String, String> {
                    salePrice, fixedCost, percentCost -> "using sale price"
            }).mergeWith(
            combineLatest(
                inputMarkup,
                inputFixedCost,
                inputPercentCost,
                Function3<String, String, String, String> {
                        markup, fixedCost, percentCost -> "using markup"
                })
        ).mergeWith(
            combineLatest(
                inputProfit,
                inputFixedCost,
                inputPercentCost,
                Function3<String, String, String, String> {
                        profit, fixedCost, percentCost -> "using profitMargin"
                })
        )
    }

    //endregion
    /*
    //This needs to be all fixed costs AND all percentage costs. Currently only cost and profit margin
    override val outputSalePrice: Observable<String> =
            combineLatest(
                    inputFixedCost,
                    inputProfitMargin,
                    BiFunction {
                        cost, profitMargin ->
                        calculateSalePrice(toBigDecimal(cost), toBigDecimal(profitMargin))
                    })

    //This needs to be all fixed costs. Currently only cost of item
    override val outputMarkup: Observable<String> =
            combineLatest(
                    inputProfit,
                    inputFixedCost,
                    BiFunction {
                        profit, cost ->
                        calculateMarkup(toBigDecimal(profit), toBigDecimal(cost))
                    })

    override val outputProfit: Observable<String> =
        combineLatest(
            inputSalePrice,
            inputProfitMargin,
            BiFunction {
                    sale, profitMargin ->
                calculateProfit(toBigDecimal(sale), toBigDecimal(profitMargin))
            })

    override val outputProfitMargin: Observable<String> =
        combineLatest(
            inputProfit,
            inputSalePrice,
            BiFunction {
                    profit, salePrice ->
                calculateProfitMargin(toBigDecimal(profit), toBigDecimal(salePrice))
            })

    */
}