package com.qzakapps.sellingpricecalc.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.qzakapps.sellingpricecalc.models.Cost
import io.reactivex.Observable


@Dao
interface CostDao {

    @Query ("SELECT * FROM Cost ORDER BY name DESC")
    fun getAllCost(): Observable<List<Cost>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCost(cost: Cost)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCostList(costList: List<Cost>)

    @Query("DELETE FROM Cost")
    fun deleteAllCost()
}