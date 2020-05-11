package com.qzakapps.sellingpricecalc.viewmodels

import android.app.Application
import com.qzakapps.sellingpricecalc.adapters.CalculationLoadTemplateRecyclerAdapter
import com.qzakapps.sellingpricecalc.helper.*
import com.qzakapps.sellingpricecalc.models.*
import io.reactivex.Observable
import io.reactivex.Observable.combineLatest
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.math.BigDecimal

interface CalculationViewModelInputs {
    val profitMargin: PublishSubject<BigDecimal>
    val salePrice: PublishSubject<BigDecimal>
    val profit: PublishSubject<BigDecimal>
    val markup: PublishSubject<BigDecimal>

    val singleCostFullString: BehaviorSubject<String>
    val singlePercentageFullString: BehaviorSubject<String>
    val profitMarginFullString: BehaviorSubject<String>
    val salePriceFullString: BehaviorSubject<String>
    val profitFullString: BehaviorSubject<String>
    val markupFullString: BehaviorSubject<String>

    val fixedCost: PublishSubject<BigDecimal>
    val percentCost: PublishSubject<BigDecimal>

    val saveTemplateBtnClicked: PublishSubject<String>
    val settingsBtnClicked: PublishSubject<Unit>
}

interface CalculationViewModelOutputs {

    val costList: BehaviorSubject<List<Cost>>
    val percentageList: BehaviorSubject<List<Percentage>>
    val templateList: Observable<List<Template>>

    fun displayFixedCost(): Observable<String>
    fun displayPercentageCost(): Observable<String>


    val singleCostOutput: BehaviorSubject<String>
    val singlePercentageOutput: BehaviorSubject<String>
    val profitMarginOutput: BehaviorSubject<String>
    val profitOutput: BehaviorSubject<String>
    val salePriceOutput: BehaviorSubject<String>
    val markupOutput: BehaviorSubject<String>

    val profitMarginError: PublishSubject<Boolean>

    fun saveTemplate(): Observable<Unit>
    val showSaveDialog: Observable<Unit>
    val showLoadDialog: Observable<Unit>
    val templateLoaded: Observable<Unit>
}

class CalculationViewModel(application: Application) :
    BaseViewModel(application),
    CalculationViewModelInputs,
    CalculationViewModelOutputs,
    CalculationLoadTemplateRecyclerAdapter.TemplateClicked {

    //region Input Variables

    //The streams that contain input values that only emit when xTextChangeByUser is called
    override val profitMargin: PublishSubject<BigDecimal> = PublishSubject.create()
    override val salePrice: PublishSubject<BigDecimal> = PublishSubject.create()
    override val profit: PublishSubject<BigDecimal> = PublishSubject.create()
    override val markup: PublishSubject<BigDecimal> = PublishSubject.create()

    //The steams that contain all updates to editText, from user or from calculations (or manually)

    override val singleCostFullString: BehaviorSubject<String> = BehaviorSubject.createDefault("")
    override val singlePercentageFullString: BehaviorSubject<String> = BehaviorSubject.createDefault("")
    override val profitMarginFullString: BehaviorSubject<String> = BehaviorSubject.createDefault("")
    override val salePriceFullString: BehaviorSubject<String> = BehaviorSubject.createDefault("")
    override val profitFullString: BehaviorSubject<String> = BehaviorSubject.createDefault("")
    override val markupFullString: BehaviorSubject<String> = BehaviorSubject.createDefault("")

    override val fixedCost: PublishSubject<BigDecimal> = PublishSubject.create()
    override val percentCost: PublishSubject<BigDecimal> = PublishSubject.create()

    override val saveTemplateBtnClicked: PublishSubject<String> = PublishSubject.create()
    override val settingsBtnClicked: PublishSubject<Unit> = PublishSubject.create()
    //endregion

    //region Output Variables
    override val costList: BehaviorSubject<List<Cost>> = BehaviorSubject.createDefault(mutableListOf())
    override val percentageList: BehaviorSubject<List<Percentage>> = BehaviorSubject.createDefault(mutableListOf())
    override val templateList: Observable<List<Template>> = repo.getAllTemplate

    //The streams that output the calculations as Strings and update texts

    override val singleCostOutput: BehaviorSubject<String> = BehaviorSubject.createDefault("")
    override val singlePercentageOutput: BehaviorSubject<String> = BehaviorSubject.createDefault("")
    override val profitMarginOutput: BehaviorSubject<String> = profitMargin()
    override val profitOutput: BehaviorSubject<String> = profit()
    override val markupOutput: BehaviorSubject<String> = markup()
    override val salePriceOutput: BehaviorSubject<String> = salePrice()

    override val profitMarginError: PublishSubject<Boolean> = PublishSubject.create()

    override val showSaveDialog: PublishSubject<Unit> = PublishSubject.create()
    override val showLoadDialog: PublishSubject<Unit> = PublishSubject.create()
    override val templateLoaded: PublishSubject<Unit> = PublishSubject.create()
    //endregion

    private val templateObservableList = listOf(
        costList,
        percentageList,
        singleCostFullString,
        singlePercentageFullString,
        markupFullString,
        salePriceFullString,
        profitFullString,
        profitMarginFullString)

    val outputs: CalculationViewModelOutputs = this

    //region logic
    override fun templateClicked(template: Template, fromDialog: Boolean) {

        costList.onNext(template.costList)
        percentageList.onNext(template.percentageList)

        singleCostOutput.onNext(template.singleCost)
        singlePercentageOutput.onNext(template.singlePercentage)

        onProfitMarginTextChangeByUser(template.profitMargin)
        profitMarginOutput.onNext(template.profitMargin)
        SharePref.templateID = template.id
        SharePref.templateName = template.name

        if (fromDialog) {
            templateLoaded.onNext(Unit)
        }

    }

    //Use the sharedPref to find the current template to load
    fun loadTemplate(): Observable<Template> {
        return repo.getTemplateById(SharePref.templateID).observeOn(AndroidSchedulers.mainThread())
    }

    //Save all changes to the current working template, uses shared pref to save template ID/Name
    fun saveCurrentTemplate(){
        combineLatest(templateObservableList){ observableList ->

            val template = Template(
                id = SharePref.templateID,
                name = SharePref.templateName,
                costList = (observableList[0] as List<Cost>),
                percentageList = (observableList[1] as List<Percentage>),
                singleCost = observableList[2] as String,
                singlePercentage = observableList[3] as String,
                markup = (observableList[4] as String),
                salePrice = (observableList[5] as String),
                profit = (observableList[6] as String),
                profitMargin = (observableList[7] as String))

            repo.insertTemplate(template)
        }.subscribe()
    }

    private fun salePrice(): BehaviorSubject<String> {
        val behaviorSubject = BehaviorSubject.createDefault("")
        combineLatest(
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
        ).observeOn(AndroidSchedulers.mainThread()).subscribe(behaviorSubject)
        return behaviorSubject
    }

    private fun markup(): BehaviorSubject<String> {
        val behaviorSubject = BehaviorSubject.createDefault("")
        combineLatest(
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
        ).observeOn(AndroidSchedulers.mainThread()).subscribe(behaviorSubject)
        return behaviorSubject
    }

    private fun profit(): BehaviorSubject<String> {
        val behaviorSubject = BehaviorSubject.createDefault("")
        combineLatest(
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
        ).observeOn(AndroidSchedulers.mainThread()).subscribe(behaviorSubject)
        return behaviorSubject
    }

    private fun profitMargin(): BehaviorSubject<String> {
        val behaviorSubject = BehaviorSubject.createDefault("")
        combineLatest(
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
        ).observeOn(AndroidSchedulers.mainThread()).subscribe(behaviorSubject)

        return behaviorSubject
    }
    //endregion

    //region inputs
    fun onSettingsBtnClicked(){
        settingsBtnClicked.onNext(Unit)
        repo.insertCost(Cost(cost = "12", name = "weleve"))
    }

    fun onSingleCostStringChanged(text: String){
        singleCostFullString.onNext(text)
    }

    fun onSinglePercentageChanged(text: String){
        singlePercentageFullString.onNext(text)
    }

    fun onProfitMarginStringChanged(text: String){
        profitMarginFullString.onNext(text)
    }

    fun onSalePriceStringChanged(text: String){
        salePriceFullString.onNext(text)
    }

    fun onProfitStringChanged(text: String){
        profitFullString.onNext(text)
    }

    fun onMarkupStringChanged(text: String){
        markupFullString.onNext(text)
    }

    fun onSaveTemplateBtnClicked(){
        showSaveDialog.onNext(Unit)
    }

    fun onLoadTemplateBtnClicked(){
        showLoadDialog.onNext(Unit)
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
                name = (observableList[0] as String),
                costList = (observableList[1] as List<Cost>),
                percentageList = (observableList[2] as List<Percentage>),
                singleCost = observableList[2] as String,
                singlePercentage = observableList[3] as String,
                markup = (observableList[4] as String),
                salePrice = (observableList[5] as String),
                profit = (observableList[6] as String),
                profitMargin = (observableList[7] as String))

            repo.insertTemplate(template)
            templateClicked(template, true)
        }
    }

    override fun displayFixedCost(): Observable<String> {
        return combineLatest(costList, singleCostFullString, BiFunction<List<Cost>, String, List<Cost>> { costList, singleCost ->
            costList + Cost(name = SINGLE_COST, cost = singleCost)
        }).map { costList ->
            var total = BigDecimal.ZERO
            costList.forEach { cost -> total += toBigDecimal(cost.cost) }
            fixedCost.onNext(total)
            return@map total.toString()
        }.observeOn(AndroidSchedulers.mainThread())
    }

    override fun displayPercentageCost(): Observable<String> {
        return combineLatest(percentageList, singlePercentageFullString, BiFunction<List<Percentage>, String, List<Percentage>> { percList, singlePerc ->
            percList + Percentage(name = SINGLE_PERCENTAGE, cost = singlePerc)
        }).map { percCost ->
            var total = BigDecimal.ZERO
            percCost.forEach { perc -> total += toBigDecimal(perc.cost) }
            percentCost.onNext(total)
            return@map total.toString()
        }.observeOn(AndroidSchedulers.mainThread())
    }

    //endregion
}