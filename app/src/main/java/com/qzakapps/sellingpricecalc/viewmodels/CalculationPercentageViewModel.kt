package com.qzakapps.sellingpricecalc.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.qzakapps.sellingpricecalc.database.AppDatabase
import com.qzakapps.sellingpricecalc.models.Percentage
import com.qzakapps.sellingpricecalc.repositories.Repository
import io.reactivex.Observable

interface CalculationPercentageViewModelInputs {
}

interface CalculationPercentageViewModelOutputs {
    fun percentageList(): Observable<List<Percentage>>
}

class CalculationPercentageViewModel(application: Application): BaseViewModel(application), CalculationPercentageViewModelInputs, CalculationPercentageViewModelOutputs {

    private val percentageList: Observable<List<Percentage>> = repo.getAllPercentage

    var inputs: CalculationPercentageViewModelInputs = this
    var outputs: CalculationPercentageViewModelOutputs = this

    //region inputs
    //endregion

    //region outputs
    override fun percentageList() = percentageList
    //endregion


}