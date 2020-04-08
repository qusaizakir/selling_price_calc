package com.qzakapps.sellingpricecalc.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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
}