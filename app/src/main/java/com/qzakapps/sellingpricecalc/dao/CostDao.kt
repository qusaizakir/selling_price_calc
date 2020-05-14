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

    @Query("DELETE FROM Cost WHERE :id = id")
    fun deleteCostByID(id: String)

    @Query("DELETE FROM Cost")
    fun deleteAllCost()

    @Query("UPDATE Cost SET active = NOT active WHERE :id = id")
    fun toggleActiveCost(id: String)

    @Query("SELECT * FROM Cost WHERE active = 1")
    fun getAllActiveCosts(): Observable<List<Cost>>

    @Query("Update Cost Set active = CASE WHEN id IN(:ids) THEN 1 ELSE 0 END")
    fun setOnlyCostsActive(ids: List<String>)
}