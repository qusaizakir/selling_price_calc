package com.qzakapps.sellingpricecalc.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.qzakapps.sellingpricecalc.database.AppDatabase
import com.qzakapps.sellingpricecalc.models.Cost
import com.qzakapps.sellingpricecalc.repositories.Repository
import io.reactivex.Observable

interface CalculationCostViewModelInputs {
}

interface CalculationCostViewModelOutputs {
    fun costList(): Observable<List<Cost>>
}

class CalculationCostViewModel(application: Application) : BaseViewModel(application), CalculationCostViewModelInputs, CalculationCostViewModelOutputs {

    private val costList: Observable<List<Cost>> = repo.getAllCost

    var inputs: CalculationCostViewModelInputs = this
    var outputs: CalculationCostViewModelOutputs = this

    //region inputs
    //endregion

    //region outputs
    override fun costList() = costList
    //endregion


}