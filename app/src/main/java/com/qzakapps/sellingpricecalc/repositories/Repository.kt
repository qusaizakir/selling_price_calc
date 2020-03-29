package com.qzakapps.sellingpricecalc.repositories

import com.qzakapps.sellingpricecalc.dao.CostDao
import com.qzakapps.sellingpricecalc.dao.PercentageDao
import com.qzakapps.sellingpricecalc.dao.TemplateDao
import com.qzakapps.sellingpricecalc.models.Cost
import com.qzakapps.sellingpricecalc.models.Percentage
import com.qzakapps.sellingpricecalc.models.Template
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class Repository(
    private val costDao: CostDao,
    private val percentageDao: PercentageDao,
    private val templateDao: TemplateDao) {

    val getAllCost: Observable<List<Cost>> = costDao.getAllCost()
    val getAllPercentage: Observable<List<Percentage>> =  percentageDao.getAllPercentage()
    val getAllTemplate: Observable<List<Template>> = templateDao.getAllTemplate()

    fun insertCost(cost: Cost) {
        Completable
            .fromAction{ costDao.insertCost(cost)}
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun insertPercentage(percentage: Percentage) {
        Completable
            .fromAction{ percentageDao.insertPercentage(percentage)}
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun insertTemplate(template: Template){
        Completable
            .fromAction{ templateDao.insertTemplate(template)}
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
}