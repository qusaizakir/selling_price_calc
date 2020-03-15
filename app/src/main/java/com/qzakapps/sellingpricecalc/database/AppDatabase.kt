package com.qzakapps.sellingpricecalc.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.qzakapps.sellingpricecalc.dao.CostDao
import com.qzakapps.sellingpricecalc.dao.PercentageDao
import com.qzakapps.sellingpricecalc.models.Cost
import com.qzakapps.sellingpricecalc.models.Percentage


@Database(entities = [Cost::class, Percentage::class], version = 1, exportSchema = false)
public abstract class AppDatabase : RoomDatabase() {

    abstract fun costDao(): CostDao
    abstract fun percentageDao(): PercentageDao

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
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}