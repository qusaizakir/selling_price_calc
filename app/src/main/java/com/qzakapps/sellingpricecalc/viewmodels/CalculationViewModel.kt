package com.qzakapps.sellingpricecalc.viewmodels

import android.app.Application
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.qzakapps.sellingpricecalc.R
import com.qzakapps.sellingpricecalc.helper.*
import com.qzakapps.sellingpricecalc.models.Cost
import com.qzakapps.sellingpricecalc.models.Percentage
import com.qzakapps.sellingpricecalc.models.Template
import io.reactivex.Observable
import io.reactivex.Observable.combineLatest
import io.reactivex.android.schedulers.AndroidSchedulers
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

    val profitMarginString: BehaviorSubject<String>
    val salePriceString: BehaviorSubject<String>
    val profitString: BehaviorSubject<String>
    val markupString: BehaviorSubject<String>

    val fixedCost: PublishSubject<BigDecimal>
    val percentCost: PublishSubject<BigDecimal>

    val saveTemplateBtnClicked: PublishSubject<Boolean>
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

    fun saveTemplate(): Observable<Unit>
    val showSaveDialog: Observable<Unit>
}

class CalculationViewModel(application: Application) : BaseViewModel(application), CalculationViewModelInputs, CalculationViewModelOutputs {

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

    override val profitMarginString: BehaviorSubject<String> = BehaviorSubject.createDefault("")
    override val salePriceString: BehaviorSubject<String> = BehaviorSubject.createDefault("")
    override val profitString: BehaviorSubject<String> = BehaviorSubject.createDefault("")
    override val markupString: BehaviorSubject<String> = BehaviorSubject.createDefault("")

    override val clearCostNameAndCost: PublishSubject<Unit> = PublishSubject.create()
    override val clearPercentageNameAndCost: PublishSubject<Unit> = PublishSubject.create()

    override val profitMarginError: PublishSubject<Boolean> = PublishSubject.create()

    override val saveTemplateBtnClicked: PublishSubject<Boolean> = PublishSubject.create()
    override val showSaveDialog: PublishSubject<Unit> = PublishSubject.create()

    private val templateObservableList = listOf(
        repo.getAllCost,
        repo.getAllPercentage,
        markupString,
        salePriceString,
        profitString,
        profitMarginString)

    val outputs: CalculationViewModelOutputs = this

    //region logic
    //endregion

    //region inputs
    fun onProfitMarginStringChanged(text: String){
        profitMarginString.onNext(text)
    }

    fun onSalePriceStringChanged(text: String){
        salePriceString.onNext(text)
    }

    fun onProfitStringChanged(text: String){
        profitString.onNext(text)
    }

    fun onMarkupStringChanged(text: String){
        markupString.onNext(text)
    }

    fun onSaveTemplateBtnClicked(){
        showSaveDialog.onNext(Unit)
    }

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

    fun onProfitMarginTextChangeByUser(text: String) {
        takeIf{ toBigDecimal(text).let{ it <= _100 && it >= _0 } }?.let {
            profitMargin.onNext(toBigDecimal(text))
            profitMarginError.onNext(false)
        } ?: profitMarginError.onNext(true)
    }

    fun onSalePriceTextChangeByUser(text: String){
        salePrice.onNext(toBigDecimal(text))
    }

    fun onProfitTextChangeByUser(text: String){
        profit.onNext(toBigDecimal(text))
    }

    fun onMarkupTextChangeByUser(text: String){
        markup.onNext(toBigDecimal(text))
    }
    //endregion

    //region outputs
    override fun saveTemplate(): Observable<Unit> {
        return saveTemplateBtnClicked.withLatestFrom(templateObservableList){ observableList ->

            val template = Template(
                name = "test:"+ System.currentTimeMillis(),
                costList = (observableList[1] as List<Cost>),
                percentageList = (observableList[2] as List<Percentage>),
                markup = (observableList[3] as String),
                salePrice = (observableList[4] as String),
                profit = (observableList[5] as String),
                profitMargin = (observableList[6] as String))

            repo.insertTemplate(template)
        }
    }

    override fun displayFixedCost(): Observable<String> {
        return repo.getAllCost.map { costList ->
            var total = BigDecimal.ZERO
            costList.forEach { cost -> total += toBigDecimal(cost.cost) }
            fixedCost.onNext(total)
            return@map total.toString()
        }.observeOn(AndroidSchedulers.mainThread())
    }

    override fun displayPercentageCost(): Observable<String> {
        return repo.getAllPercentage.map { percCost ->
            var total = BigDecimal.ZERO
            percCost.forEach { perc -> total += toBigDecimal(perc.cost) }
            percentCost.onNext(total)
            return@map total.toString()
        }.observeOn(AndroidSchedulers.mainThread())
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
        return addCostBtnClicked.withLatestFrom(costName, costValue, Function3 {
            _, name, value ->
            val cost = Cost(name = name, cost = value)
            repo.insertCost(cost)
        })
    }

    override fun insertPercentage(): Observable<Unit> {
        return addPercentageBtnClicked.withLatestFrom(percentageName, percentageValue, Function3 {
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
        ).observeOn(AndroidSchedulers.mainThread())
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
        ).observeOn(AndroidSchedulers.mainThread())
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
         ).observeOn(AndroidSchedulers.mainThread())
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
        ).observeOn(AndroidSchedulers.mainThread())
    }

    //endregion
}