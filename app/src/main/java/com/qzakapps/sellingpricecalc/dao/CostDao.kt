package com.qzakapps.sellingpricecalc.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.qzakapps.sellingpricecalc.models.Cost
import io.reactivex.Flowable


@Dao
interface CostDao {

    @Query ("SELECT * FROM Cost ORDER BY name DESC")
    fun getAllCost(): Flowable<List<Cost>>

    @Insert
    fun insertCost(cost: Cost)
}