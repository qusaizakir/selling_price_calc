package com.qzakapps.sellingpricecalc.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.qzakapps.sellingpricecalc.database.AppDatabase
import com.qzakapps.sellingpricecalc.models.Cost
import com.qzakapps.sellingpricecalc.repositories.Repository
import io.reactivex.Flowable

interface CalculationCostViewModelInputs {
}

interface CalculationCostViewModelOutputs {
    fun costList(): Flowable<List<Cost>>
}

class CalculationCostViewModel(application: Application) : AndroidViewModel(application), CalculationCostViewModelInputs, CalculationCostViewModelOutputs {

    private val repo: Repository
    init {
        val costDao = AppDatabase.getDatabase(application).costDao()
        val percentageDao = AppDatabase.getDatabase(application).percentageDao()
        repo = Repository(costDao, percentageDao)
    }

    private val costList: Flowable<List<Cost>> = repo.getAllCost

    var inputs: CalculationCostViewModelInputs = this
    var outputs: CalculationCostViewModelOutputs = this

    //region inputs
    //endregion

    //region outputs
    override fun costList() = costList
    //endregion


}