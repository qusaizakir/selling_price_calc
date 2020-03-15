package com.qzakapps.sellingpricecalc.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.qzakapps.sellingpricecalc.models.Percentage
import io.reactivex.Flowable

@Dao
interface PercentageDao {

    @Query ("SELECT * FROM Percentage ORDER BY name DESC")
    fun getAllPercentage(): Flowable<List<Percentage>>

    @Insert
    fun insertPercentage(percentage: Percentage)
}