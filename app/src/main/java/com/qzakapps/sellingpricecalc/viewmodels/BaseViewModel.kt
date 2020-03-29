package com.qzakapps.sellingpricecalc.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.qzakapps.sellingpricecalc.database.AppDatabase
import com.qzakapps.sellingpricecalc.repositories.Repository

abstract class BaseViewModel(application: Application) : AndroidViewModel(application){

    protected val repo: Repository
    init {
        val costDao = AppDatabase.getDatabase(application).costDao()
        val percentageDao = AppDatabase.getDatabase(application).percentageDao()
        val templateDao = AppDatabase.getDatabase(application).templateDao()
        repo = Repository(costDao, percentageDao, templateDao)
    }
}