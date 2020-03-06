package com.qzakapps.sellingpricecalc.viewmodels

import androidx.lifecycle.ViewModel
import com.qzakapps.sellingpricecalc.helper.*
import com.qzakapps.sellingpricecalc.models.CostModel
import com.qzakapps.sellingpricecalc.models.PercentageModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observable.combineLatest
import io.reactivex.rxjava3.functions.Function3
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import java.math.BigDecimal

interface CalculationPercentageViewModelInputs {
    fun addPercentageModelBtnClick(percentageModel: PercentageModel)
}

interface CalculationPercentageViewModelOutputs {
    fun percentageModelList(): BehaviorSubject<List<PercentageModel>>
}

class CalculationPercentageViewModel: ViewModel(), CalculationPercentageViewModelInputs, CalculationPercentageViewModelOutputs {

    private val percentageModelList: BehaviorSubject<List<PercentageModel>> = BehaviorSubject.createDefault(emptyList())

    var inputs: CalculationPercentageViewModelInputs = this
    var outputs: CalculationPercentageViewModelOutputs = this

    //region inputs
    override fun addPercentageModelBtnClick(percentageModel: PercentageModel) {
        percentageModelList.onNext(percentageModelList.value.plus(percentageModel))
    }
    //endregion

    //region outputs
    override fun percentageModelList() = percentageModelList
    //endregion


}