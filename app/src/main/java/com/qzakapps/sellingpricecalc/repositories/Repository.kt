package com.qzakapps.sellingpricecalc.repositories

import com.qzakapps.sellingpricecalc.dao.CostDao
import com.qzakapps.sellingpricecalc.dao.PercentageDao
import com.qzakapps.sellingpricecalc.models.Cost
import com.qzakapps.sellingpricecalc.models.Percentage
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class Repository(
    private val costDao: CostDao,
    private val percentageDao: PercentageDao) {

    val getAllCost: Observable<List<Cost>> = costDao.getAllCost()
    val getAllPercentage: Observable<List<Percentage>> =  percentageDao.getAllPercentage()

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
}