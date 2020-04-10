package com.qzakapps.sellingpricecalc.helper

import android.content.Context
import android.content.SharedPreferences
import com.qzakapps.sellingpricecalc.models.UNSAVED_TEMPLATE_ID

object SharePref {

    private const val FILE_NAME = "com.qzakapps.sellingpricecalc"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var pref: SharedPreferences

    private val TEMPLATE_ID = Pair("TEMPLATE_ID", UNSAVED_TEMPLATE_ID)
    private val TEMPLATE_NAME = Pair("TEMPLATE_NAME", UNSAVED_TEMPLATE_ID)

    fun init(context: Context) {
        pref  = context.getSharedPreferences(FILE_NAME, MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var templateID: String
        get() = pref.getString(TEMPLATE_ID.first, TEMPLATE_ID.second) ?: UNSAVED_TEMPLATE_ID
        set(value) = pref.edit { editor ->
            editor.putString(TEMPLATE_ID.first, value)
        }

    var templateName: String
        get() = pref.getString(TEMPLATE_NAME.first, TEMPLATE_NAME.second) ?: UNSAVED_TEMPLATE_ID
        set(value) = pref.edit { editor ->
            editor.putString(TEMPLATE_NAME.first, value)
        }
}