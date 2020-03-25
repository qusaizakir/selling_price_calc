package com.qzakapps.sellingpricecalc.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.qzakapps.sellingpricecalc.database.AppDatabase
import com.qzakapps.sellingpricecalc.helper.*
import com.qzakapps.sellingpricecalc.models.Cost
import com.qzakapps.sellingpricecalc.models.Percentage
import com.qzakapps.sellingpricecalc.repositories.Repository
import io.reactivex.Observable
import io.reactivex.Observable.combineLatest
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.math.BigDecimal

interface CalculationViewModelInputs {
    val addCostBtnClicked: PublishSubject<Boolean>
    val costName: BehaviorSubject<String>
    val costValue: BehaviorSubject<String>

    val addPercentageBtnClicked: PublishSubject<Boolean>
    val percentageName: BehaviorSubject<String>
    val percentageValue: BehaviorSubject<String>

    val profitMargin: PublishSubject<BigDecimal>
    val salePrice: PublishSubject<BigDecimal>
    val profit: PublishSubject<BigDecimal>
    val markup: PublishSubject<BigDecimal>

    val fixedCost: PublishSubject<BigDecimal>
    val percentCost: PublishSubject<BigDecimal>
}

interface CalculationViewModelOutputs {
    fun insertCost(): Observable<Unit>
    fun insertPercentage(): Observable<Unit>
    val clearCostNameAndCost: Observable<Unit>
    val clearPercentageNameAndCost: Observable<Unit>
    fun costBtnEnabled(): Observable<Boolean>
    fun percentageBtnEnabled(): Observable<Boolean>

    fun displayFixedCost(): Observable<String>
    fun displayPercentageCost(): Observable<String>

    fun salePrice(): Observable<String>
    fun markup(): Observable<String>
    fun profit(): Observable<String>
    fun profitMargin(): Observable<String>

    val profitMarginError: PublishSubject<Boolean>
}

class CalculationViewModel(application: Application) : AndroidViewModel(application), CalculationViewModelInputs, CalculationViewModelOutputs {

    private val repo: Repository
    init {
        val costDao = AppDatabase.getDatabase(application).costDao()
        val percentageDao = AppDatabase.getDatabase(application).percentageDao()
        repo = Repository(costDao, percentageDao)
    }

    override val fixedCost: PublishSubject<BigDecimal> = PublishSubject.create()
    override val percentCost: PublishSubject<BigDecimal> = PublishSubject.create()

    override val addCostBtnClicked: PublishSubject<Boolean> = PublishSubject.create()
    override val costName: BehaviorSubject<String> = BehaviorSubject.createDefault("")
    override val costValue: BehaviorSubject<String> = BehaviorSubject.createDefault("")

    override val addPercentageBtnClicked: PublishSubject<Boolean> = PublishSubject.create()
    override val percentageName: BehaviorSubject<String> = BehaviorSubject.createDefault("")
    override val percentageValue: BehaviorSubject<String> = BehaviorSubject.createDefault("")

    override val profitMargin: PublishSubject<BigDecimal> = PublishSubject.create()
    override val salePrice: PublishSubject<BigDecimal> = PublishSubject.create()
    override val profit: PublishSubject<BigDecimal> = PublishSubject.create()
    override val markup: PublishSubject<BigDecimal> = PublishSubject.create()

    override val clearCostNameAndCost: PublishSubject<Unit> = PublishSubject.create()
    override val clearPercentageNameAndCost: PublishSubject<Unit> = PublishSubject.create()

    override val profitMarginError: PublishSubject<Boolean> = PublishSubject.create()

    val outputs: CalculationViewModelOutputs = this

    //region inputs
    fun onAddCostBtnClicked(){
        addCostBtnClicked.onNext(true)
        clearCostNameAndCost.onNext(Unit)
    }

    fun onCostNameTextChange(text: String){
        costName.onNext(text)
    }

    fun onCostValueTextChange(text: String){
        costValue.onNext(text)
    }

    fun onAddPercentageBtnClicked(){
        addPercentageBtnClicked.onNext(true)
        clearPercentageNameAndCost.onNext(Unit)
    }

    fun onPercentageNameTextChange(text: String){
        percentageName.onNext(text)
    }

    fun onPercentageValueTextChange(text: String){
        percentageValue.onNext(text)
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

    override fun displayFixedCost(): Observable<String> {
        return repo.getAllCost.map { costList ->
            var total = BigDecimal.ZERO
            costList.forEach { cost -> total += toBigDecimal(cost.cost) }
            fixedCost.onNext(total)
            return@map total.toString()
        }
    }

    override fun displayPercentageCost(): Observable<String> {
        return repo.getAllPercentage.map { percCost ->
            var total = BigDecimal.ZERO
            percCost.forEach { perc -> total += toBigDecimal(perc.cost) }
            percentCost.onNext(total)
            return@map total.toString()
        }
    }

    override fun costBtnEnabled(): Observable<Boolean> {
        return combineLatest(
            costName,
            costValue,
            BiFunction<String, String, Boolean> { name, value -> name.isNotEmpty() && value.isNotEmpty() })
    }

    override fun percentageBtnEnabled(): Observable<Boolean> {
        return combineLatest(
            percentageName,
            percentageValue,
            BiFunction<String, String, Boolean> { name, value -> name.isNotEmpty() && value.isNotEmpty() })
    }

    override fun insertCost(): Observable<Unit> {
        return addCostBtnClicked.withLatestFrom(costName, costValue, Function3<Boolean, String, String, Unit>{
            _, name, value ->
            val cost = Cost(name = name, cost = value)
            repo.insertCost(cost)
        })
    }

    override fun insertPercentage(): Observable<Unit> {
        return addPercentageBtnClicked.withLatestFrom(percentageName, percentageValue, Function3<Boolean, String, String, Unit>{
                _, name, value ->
            val percentage = Percentage(name = name, cost = value)
            repo.insertPercentage(percentage)
        })
    }

    override fun salePrice(): Observable<String> {
        return combineLatest(
            markup,
            fixedCost,
            percentCost,
            Function3<BigDecimal, BigDecimal, BigDecimal, String>{
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