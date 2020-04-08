package com.qzakapps.sellingpricecalc.helper

import android.content.Context
import android.content.SharedPreferences

inline fun <reified T> SharedPreferences.get(key: String, defaultValue: T): T {
    when(T::class) {
        Boolean::class -> return this.getBoolean(key, defaultValue as Boolean) as T
        Float::class -> return this.getFloat(key, defaultValue as Float) as T
        Int::class -> return this.getInt(key, defaultValue as Int) as T
        Long::class -> return this.getLong(key, defaultValue as Long) as T
        String::class -> return this.getString(key, defaultValue as String) as T
        else -> {
            if (defaultValue is Set<*>) {
                return this.getStringSet(key, defaultValue as Set<String>) as T
            }
        }
    }

    return defaultValue
}

inline fun <reified T> SharedPreferences.put(key: String, value: T){
    val editor = this.edit()

    when(T::class) {
        Boolean::class -> editor.putBoolean(key, value as Boolean)
        Float::class -> editor.putFloat(key, value as Float)
        Int::class -> editor.putInt(key, value as Int)
        Long::class -> editor.putLong(key, value as Long)
        String::class -> editor.putString(key, value as String)
        else -> {
            if (value is Set<*>) {
                editor.putStringSet(key, value as Set<String>)
            }
        }
    }

    editor.apply()
}

class SharePref (context: Context){

    companion object{
        const val TEMPLATE_ID = "template_id"
    }

    private val fileName = "com.qzakapps.sellingpricecalc"
    private val pref: SharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)

    fun put(key: String, value: Any) = pref.put(key, value)
    fun get(key: String, defaultValue: Any) = pref.get(key, defaultValue)
}