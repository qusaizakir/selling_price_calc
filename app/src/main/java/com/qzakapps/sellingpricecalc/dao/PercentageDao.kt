package com.qzakapps.sellingpricecalc.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.qzakapps.sellingpricecalc.models.Percentage
import io.reactivex.Observable

@Dao
interface PercentageDao {

    @Query ("SELECT * FROM Percentage ORDER BY name DESC")
    fun getAllPercentage(): Observable<List<Percentage>>

    @Insert
    fun insertPercentage(percentage: Percentage)
}