package com.qzakapps.sellingpricecalc.viewmodels

import android.app.Application

interface PercentageListInputs {

}

interface PercentageListOutputs {

}

class PercentageListViewModel(application: Application) :
    BaseViewModel(application),
    PercentageListInputs,
    PercentageListOutputs{

}