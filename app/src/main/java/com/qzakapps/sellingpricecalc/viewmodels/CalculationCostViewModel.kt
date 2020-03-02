package com.qzakapps.sellingpricecalc.viewmodels

import androidx.lifecycle.ViewModel
import com.qzakapps.sellingpricecalc.helper.*
import com.qzakapps.sellingpricecalc.models.CostModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observable.combineLatest
import io.reactivex.rxjava3.functions.Function3
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import java.math.BigDecimal

interface CalculationCostViewModelInputs {
    fun addCostModelBtnClick(costModel: CostModel)
}

interface CalculationCostViewModelOutputs {
    fun costModelList(): BehaviorSubject<List<CostModel>>
}

class CalculationCostViewModel : ViewModel(), CalculationCostViewModelInputs, CalculationCostViewModelOutputs {

    private val costModelList: BehaviorSubject<List<CostModel>> = BehaviorSubject.createDefault(emptyList())

    var inputs: CalculationCostViewModelInputs = this
    var outputs: CalculationCostViewModelOutputs = this

    //region inputs
    override fun addCostModelBtnClick(costModel: CostModel) {
        costModelList.onNext(costModelList.value.plus(costModel))
    }
    //endregion

    //region outputs
    override fun costModelList() = costModelList
    //endregion


}