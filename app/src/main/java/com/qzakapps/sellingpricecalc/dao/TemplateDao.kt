package com.qzakapps.sellingpricecalc.dao

import androidx.room.*
import com.qzakapps.sellingpricecalc.models.Template
import io.reactivex.Observable
import io.reactivex.Single


@Dao
interface TemplateDao {

    @Query ("SELECT * FROM Template ORDER BY name DESC")
    fun getAllTemplate(): Observable<List<Template>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTemplate(template: Template)

    @Update
    fun updateTemplate(template: Template)

    @Query("DELETE FROM Template")
    fun deleteAllTemplates()

    @Query("SELECT * FROM Template WHERE :id = id")
    fun getTemplateById(id: String): Single<Template>
}