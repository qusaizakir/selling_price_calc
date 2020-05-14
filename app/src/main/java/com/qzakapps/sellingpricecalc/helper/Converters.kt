package com.qzakapps.sellingpricecalc.helper

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.qzakapps.sellingpricecalc.models.Cost
import com.qzakapps.sellingpricecalc.models.Percentage

class Converters {

    @TypeConverter
    fun costListToString(list: List<Cost>) = Gson().toJson(list)

    @TypeConverter
    fun stringToCostList(string: String) = Gson().fromJson(string, Array<Cost>::class.java).toList()

    @TypeConverter
    fun percListToString(list: List<Percentage>) = Gson().toJson(list)

    @TypeConverter
    fun stringToPercList(string: String) = Gson().fromJson(string, Array<Percentage>::class.java).toList()

    @TypeConverter
    fun stringListToString(list: List<String>) = Gson().toJson(list)

    @TypeConverter
    fun stringToStringList(string: String) = Gson().fromJson(string, Array<String>::class.java).toList()
}
