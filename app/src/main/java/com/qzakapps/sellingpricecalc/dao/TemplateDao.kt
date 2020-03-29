package com.qzakapps.sellingpricecalc.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.qzakapps.sellingpricecalc.models.Template
import io.reactivex.Completable
import io.reactivex.Observable


@Dao
interface TemplateDao {

    @Query ("SELECT * FROM Template ORDER BY name DESC")
    fun getAllTemplate(): Observable<List<Template>>

    @Insert
    fun insertTemplate(template: Template)
}