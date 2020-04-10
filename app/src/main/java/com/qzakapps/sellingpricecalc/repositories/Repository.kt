package com.qzakapps.sellingpricecalc.repositories

import com.qzakapps.sellingpricecalc.dao.CostDao
import com.qzakapps.sellingpricecalc.dao.PercentageDao
import com.qzakapps.sellingpricecalc.dao.TemplateDao
import com.qzakapps.sellingpricecalc.models.Cost
import com.qzakapps.sellingpricecalc.models.Percentage
import com.qzakapps.sellingpricecalc.models.Template
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class Repository(
    private val costDao: CostDao,
    private val percentageDao: PercentageDao,
    private val templateDao: TemplateDao) {

    val getAllCost: Observable<List<Cost>> = costDao.getAllCost()
    val getAllPercentage: Observable<List<Percentage>> =  percentageDao.getAllPercentage()
    val getAllTemplate: Observable<List<Template>> = templateDao.getAllTemplate()

    //region Cost methods
    fun insertCost(cost: Cost) {
        Completable
            .fromAction{ costDao.insertCost(cost)}
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun insertCostList(costList: List<Cost>){
        Completable
                .fromAction{ costDao.insertCostList(costList)}
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

    fun deleteAllCost(){
        Completable
            .fromAction{ costDao.deleteAllCost() }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
    //endregion

    //region Percentage methods
    fun insertPercentage(percentage: Percentage) {
        Completable
            .fromAction{ percentageDao.insertPercentage(percentage)}
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun insertPercentageList(percentageList: List<Percentage>){
        Completable
                .fromAction{percentageDao.insertPercentageList(percentageList)}
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

    fun deleteAllPercentage(){
        Completable
            .fromAction{ percentageDao.deleteAllPercentage()}
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
    //endregion

    //region Template methods
    fun insertTemplate(template: Template){
        Completable
            .fromAction{ templateDao.insertTemplate(template)}
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun updateTemplate(template: Template){
        Completable
            .fromAction{ templateDao.updateTemplate(template)}
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun deleteAllTemplates(){
        Completable
            .fromAction { templateDao.deleteAllTemplates() }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun getTemplateById(id: String): Single<Template>{
        return templateDao.getTemplateById(id)
    }
    //endregion
}