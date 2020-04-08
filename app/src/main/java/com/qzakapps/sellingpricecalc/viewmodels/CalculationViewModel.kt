package com.qzakapps.sellingpricecalc.viewmodels

import android.app.Application
import com.qzakapps.sellingpricecalc.BaseApplication
import com.qzakapps.sellingpricecalc.adapters.CalculationLoadTemplateRecyclerAdapter
import com.qzakapps.sellingpricecalc.helper.*
import com.qzakapps.sellingpricecalc.models.Cost
import com.qzakapps.sellingpricecalc.models.Percentage
import com.qzakapps.sellingpricecalc.models.Template
import com.qzakapps.sellingpricecalc.models.UNSAVED_TEMPLATE_ID
import io.reactivex.Observable
import io.reactivex.Observable.combineLatest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
import io.reactivex.functions.Function4
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
    fun insertCost(): Observable<Unit>
    fun insertPercentage(): Observable<Unit>
    val costList: BehaviorSubject<List<Cost>>
    val percentageList: BehaviorSubject<List<Percentage>>
    val templateList: Observable<List<Template>>
    val clearCostNameAndCost: Observable<Unit>
    val clearPercentageNameAndCost: Observable<Unit>
    fun costBtnEnabled(): Observable<Boolean>
    fun percentageBtnEnabled(): Observable<Boolean>

    fun displayFixedCost(): Observable<String>
    fun displayPercentageCost(): Observable<String>

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
    override val addCostBtnClicked: PublishSubject<Boolean> = PublishSubject.create()
    override val costName: BehaviorSubject<String> = BehaviorSubject.createDefault("")
    override val costValue: BehaviorSubject<String> = BehaviorSubject.createDefault("")

    override val addPercentageBtnClicked: PublishSubject<Boolean> = PublishSubject.create()
    override val percentageName: BehaviorSubject<String> = BehaviorSubject.createDefault("")
    override val percentageValue: BehaviorSubject<String> = BehaviorSubject.createDefault("")

    //The streams that contain input values that only emit when xTextChangeByUser is called
    override val profitMargin: PublishSubject<BigDecimal> = PublishSubject.create()
    override val salePrice: PublishSubject<BigDecimal> = PublishSubject.create()
    override val profit: PublishSubject<BigDecimal> = PublishSubject.create()
    override val markup: PublishSubject<BigDecimal> = PublishSubject.create()

    //The steams that contain all updates to editText, from user or from calculations (or manually)
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
    override val profitMarginOutput: BehaviorSubject<String> = profitMargin()
    override val profitOutput: BehaviorSubject<String> = profit()
    override val markupOutput: BehaviorSubject<String> = markup()
    override val salePriceOutput: BehaviorSubject<String> = salePrice()

    override val clearCostNameAndCost: PublishSubject<Unit> = PublishSubject.create()
    override val clearPercentageNameAndCost: PublishSubject<Unit> = PublishSubject.create()

    override val profitMarginError: PublishSubject<Boolean> = PublishSubject.create()

    override val showSaveDialog: PublishSubject<Unit> = PublishSubject.create()
    override val showLoadDialog: PublishSubject<Unit> = PublishSubject.create()
    override val templateLoaded: PublishSubject<Unit> = PublishSubject.create()
    //endregion

    private val templateObservableList = listOf(
        costList,
        percentageList,
        markupFullString,
        salePriceFullString,
        profitFullString,
        profitMarginFullString)

    val outputs: CalculationViewModelOutputs = this

    //region logic
    override fun templateClicked(template: Template, fromDialog: Boolean) {

        costList.onNext(template.costList)
        percentageList.onNext(template.percentageList)
        onProfitMarginTextChangeByUser(template.profitMargin)
        profitMarginOutput.onNext(template.profitMargin)

        if (fromDialog) templateLoaded.onNext(Unit)

    }

    fun loadTemplate(): Observable<Template> {
        val templateID: String = BaseApplication.sharedPref.get(SharePref.TEMPLATE_ID, UNSAVED_TEMPLATE_ID) as String
        return repo.getTemplateById(templateID).observeOn(AndroidSchedulers.mainThread())
    }

    fun saveCurrentTemplateOnClose(){
        combineLatest(templateObservableList){ observableList ->

            val template = Template(
                id = UNSAVED_TEMPLATE_ID,
                name = UNSAVED_TEMPLATE_ID,
                costList = (observableList[0] as List<Cost>),
                percentageList = (observableList[1] as List<Percentage>),
                markup = (observableList[2] as String),
                salePrice = (observableList[3] as String),
                profit = (observableList[4] as String),
                profitMargin = (observableList[5] as String))

            repo.updateTemplate(template)
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
        repo.deleteAllTemplates()
        //settingsBtnClicked.onNext(Unit)
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
                name = (observableList[0] as String),
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
        return costList.map { costList ->
            var total = BigDecimal.ZERO
            costList.forEach { cost -> total += toBigDecimal(cost.cost) }
            fixedCost.onNext(total)
            return@map total.toString()
        }.observeOn(AndroidSchedulers.mainThread())
    }

    override fun displayPercentageCost(): Observable<String> {
        return percentageList.map { percCost ->
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
        return addCostBtnClicked.withLatestFrom(costName, costValue, costList,  Function4 {
                _, name, value, list ->
            val cost = Cost(name = name, cost = value)
            costList.onNext(list + cost)
        })
    }

    override fun insertPercentage(): Observable<Unit> {
        return addPercentageBtnClicked.withLatestFrom(percentageName, percentageValue, percentageList ,Function4 {
                _, name, value, list ->
            val percentage = Percentage(name = name, cost = value)
            percentageList.onNext(list + percentage)
        })
    }

    //endregion
}