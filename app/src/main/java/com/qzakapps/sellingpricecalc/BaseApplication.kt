package com.qzakapps.sellingpricecalc

import android.app.Application
import com.qzakapps.sellingpricecalc.helper.SharePref

class BaseApplication : Application() {

    companion object {
        lateinit var sharedPref: SharePref
    }

    override fun onCreate() {
        SharePref.init(applicationContext)
        super.onCreate()
    }
}