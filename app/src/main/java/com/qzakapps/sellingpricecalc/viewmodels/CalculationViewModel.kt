package com.qzakapps.sellingpricecalc.viewmodels

import androidx.lifecycle.ViewModel
import com.qzakapps.sellingpricecalc.helper.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observable.combineLatest
import io.reactivex.rxjava3.functions.Function3
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import java.math.BigDecimal

interface CalculationViewModelInputs {
    val fixedCost: PublishSubject<BigDecimal>
    val percentCost: BehaviorSubject<BigDecimal>

    val profitMargin: PublishSubject<BigDecimal>
    val salePrice: PublishSubject<BigDecimal>
    val profit: PublishSubject<BigDecimal>
    val markup: PublishSubject<BigDecimal>

}

interface CalculationViewModelOutputs {
    fun salePrice(): Observable<String>
    fun markup(): Observable<String>
    fun profit(): Observable<String>
    fun profitMargin(): Observable<String>
    fun percentCost(): Observable<String>

    val profitMarginError: PublishSubject<Boolean>
}

class CalculationViewModel : ViewModel(), CalculationViewModelInputs, CalculationViewModelOutputs {

    override val fixedCost: PublishSubject<BigDecimal> = PublishSubject.create()
    override val percentCost: BehaviorSubject<BigDecimal> = BehaviorSubject.createDefault(_0)

    override val profitMargin: PublishSubject<BigDecimal> = PublishSubject.create()
    override val salePrice: PublishSubject<BigDecimal> = PublishSubject.create()
    override val profit: PublishSubject<BigDecimal> = PublishSubject.create()
    override val markup: PublishSubject<BigDecimal> = PublishSubject.create()

    override val profitMarginError: PublishSubject<Boolean> = PublishSubject.create()

    val  inputs: CalculationViewModelInputs = this
    val outputs: CalculationViewModelOutputs = this

    //region inputs
    fun onFixedCostTextChange(text: String){
        fixedCost.onNext(toBigDecimal(text))
    }

    fun onPercentCostTextChange(text: String){
        percentCost.onNext(toBigDecimal(text))
    }

    fun onProfitMarginTextChange(text: String) {
        takeIf{ toBigDecimal(text).let{ it <= _100 && it >= _0 } }?.let {
            profitMargin.onNext(toBigDecimal(text))
            profitMarginError.onNext(false)
        } ?: profitMarginError.onNext(true)
    }

    fun onSalePriceTextChange(text: String){
        salePrice.onNext(toBigDecimal(text))
    }

    fun onProfitTextChange(text: String){
        profit.onNext(toBigDecimal(text))
    }

    fun onMarkupTextChange(text: String){
        markup.onNext(toBigDecimal(text))
    }

    //endregion

    //region outputs
    override fun percentCost(): Observable<String> = percentCost.map { bd -> bd.toString()}

    override fun salePrice(): Observable<String> {
        return combineLatest(
            markup,
            fixedCost,
            percentCost,
            Function3<BigDecimal, BigDecimal, BigDecimal, String> {
                    markup, fixedCost, percentCost ->
                calculateSalePriceWithMarkup(markup, fixedCost, percentCost)
            }).mergeWith(
            combineLatest(
                profit,
                fixedCost,
                percentCost,
                Function3<BigDecimal, BigDecimal, BigDecimal, String> {
                        profit, fixedCost, percentCost ->
                    calculateSalePriceWithProfit(profit, fixedCost, percentCost)
                })
        ).mergeWith(
            combineLatest(
                profitMargin,
                fixedCost,
                percentCost,
                Function3<BigDecimal, BigDecimal, BigDecimal, String> {
                        profitMargin, fixedCost, percentCost ->
                    calculateSalePriceWithProfitMargin(profitMargin, fixedCost, percentCost)
                })
        )
    }

    override fun markup(): Observable<String> {
        return combineLatest(
            salePrice,
            fixedCost,
            percentCost,
            Function3<BigDecimal, BigDecimal, BigDecimal, String> {
                    salePrice, fixedCost, percentCost ->
                calculateMarkupWithSalePrice(salePrice, fixedCost, percentCost)
            }).mergeWith(
            combineLatest(
                profit,
                fixedCost,
                percentCost,
                Function3<BigDecimal, BigDecimal, BigDecimal, String> {
                        profit, fixedCost, percentCost ->
                    calculateMarkupWithProfit(profit, fixedCost, percentCost)
                })
        ).mergeWith(
            combineLatest(
                profitMargin,
                fixedCost,
                percentCost,
                Function3<BigDecimal, BigDecimal, BigDecimal, String> {
                        profitMargin, fixedCost, percentCost ->
                    calculateMarkupWithProfitMargin(profitMargin, fixedCost, percentCost)
                })
        )
    }

    override fun profit(): Observable<String> {
         return combineLatest(
            salePrice,
            fixedCost,
            percentCost,
            Function3<BigDecimal, BigDecimal, BigDecimal, String> {
                salePrice, fixedCost, percentCost ->
                    calculateProfitWithSalePrice(salePrice, fixedCost, percentCost)
            }).mergeWith(
             combineLatest(
                 markup,
                 fixedCost,
                 percentCost,
                 Function3<BigDecimal, BigDecimal, BigDecimal, String> {
                         markup, fixedCost, percentCost ->
                     calculateProfitWithMarkup(markup, fixedCost, percentCost)
                 })
            ).mergeWith(
             combineLatest(
                 profitMargin,
                 fixedCost,
                 percentCost,
                 Function3<BigDecimal, BigDecimal, BigDecimal, String> {
                         profitMargin, fixedCost, percentCost ->
                    calculateProfitWithProfitMargin(profitMargin, fixedCost, percentCost)
                 })
         )
    }

    override fun profitMargin(): Observable<String> {
        return combineLatest(
            salePrice,
            fixedCost,
            percentCost,
            Function3<BigDecimal, BigDecimal, BigDecimal, String> {
                    salePrice, fixedCost, percentCost ->

                    if(salePrice != BigDecimal.ZERO) calculateProfitMarginWithSalePrice(salePrice, fixedCost, percentCost)
                    else "Infinity"

            }).mergeWith(
            combineLatest(
                markup,
                fixedCost,
                percentCost,
                Function3<BigDecimal, BigDecimal, BigDecimal, String> {
                        markup, fixedCost, percentCost ->

                    if(fixedCost != BigDecimal.ZERO) calculateProfitMarginWithMarkup(markup, fixedCost, percentCost)
                    else "Infinity"

                })).mergeWith(
                combineLatest(
                    profit,
                    fixedCost,
                    percentCost,
                    Function3<BigDecimal, BigDecimal, BigDecimal, String> {
                            profit, fixedCost, percentCost ->

                        if(fixedCost != BigDecimal.ZERO) calculateProfitMarginWithProfit(profit, fixedCost, percentCost)
                        else "Infinity"
                    })
        )
    }

    //endregion
}