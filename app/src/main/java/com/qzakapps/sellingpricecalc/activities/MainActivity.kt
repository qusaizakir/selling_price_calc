package com.qzakapps.sellingpricecalc.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.qzakapps.sellingpricecalc.R
import com.qzakapps.sellingpricecalc.fragments.CalculationFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CalculationFragment.newInstance())
                .commitNow()
        }
    }
}