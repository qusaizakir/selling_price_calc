package com.qzakapps.sellingpricecalc.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.qzakapps.sellingpricecalc.models.Cost
import com.qzakapps.sellingpricecalc.models.Percentage
import io.reactivex.Observable

@Dao
interface PercentageDao {

    @Query ("SELECT * FROM Percentage ORDER BY name DESC")
    fun getAllPercentage(): Observable<List<Percentage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPercentage(percentage: Percentage)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPercentageList(percentageList: List<Percentage>)

    @Query("DELETE FROM PERCENTAGE")
    fun deleteAllPercentage()

    @Query("DELETE FROM Percentage WHERE :id = id")
    fun deletePercentageByID(id: String)

    @Query("UPDATE Percentage SET active = NOT active WHERE :id = id")
    fun toggleActivePercentage(id: String)

    @Query("SELECT * FROM Percentage WHERE active = 1")
    fun getAllActivePercentages(): Observable<List<Percentage>>
}