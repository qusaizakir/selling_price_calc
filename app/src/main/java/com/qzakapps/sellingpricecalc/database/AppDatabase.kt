package com.qzakapps.sellingpricecalc.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.qzakapps.sellingpricecalc.dao.CostDao
import com.qzakapps.sellingpricecalc.dao.PercentageDao
import com.qzakapps.sellingpricecalc.dao.TemplateDao
import com.qzakapps.sellingpricecalc.helper.Converters
import com.qzakapps.sellingpricecalc.models.Cost
import com.qzakapps.sellingpricecalc.models.Percentage
import com.qzakapps.sellingpricecalc.models.Template


@Database(entities = [Cost::class, Percentage::class, Template::class], version = 4, exportSchema = true)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun costDao(): CostDao
    abstract fun percentageDao(): PercentageDao
    abstract fun templateDao(): TemplateDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "database"
                )
                    .fallbackToDestructiveMigration()
                    .createFromAsset("database.db")
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}