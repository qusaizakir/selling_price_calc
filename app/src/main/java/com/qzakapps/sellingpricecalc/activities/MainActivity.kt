package com.qzakapps.sellingpricecalc.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.idescout.sql.SqlScoutServer
import com.qzakapps.sellingpricecalc.R
import com.qzakapps.sellingpricecalc.fragments.CalculationFragment
import com.qzakapps.sellingpricecalc.fragments.CostListFragment
import com.qzakapps.sellingpricecalc.fragments.PercentageListFragment
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {

    val COST_FRAGMENT_ID = 0;
    val CALC_FRAGMENT_ID = 1;
    val PERC_FRAGMENT_ID = 2;

    override fun onCreate(savedInstanceState: Bundle?) {
        SqlScoutServer.create(this, packageName);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.costListMenuItem -> {
                    goToFragment(COST_FRAGMENT_ID)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.calculationMenuItem ->{
                    goToFragment(CALC_FRAGMENT_ID)
            return@setOnNavigationItemSelectedListener true
        }
        R.id.percentageListMenuItem -> {
            goToFragment(PERC_FRAGMENT_ID)
            return@setOnNavigationItemSelectedListener true
        }
        else -> false
    }
        }

        bottom_navigation.selectedItemId = R.id.calculationMenuItem
        goToFragment(CALC_FRAGMENT_ID)
    }

    private fun goToFragment(id: Int){
        val fragment = when(id){
            COST_FRAGMENT_ID -> CostListFragment.newInstance()
            CALC_FRAGMENT_ID -> CalculationFragment.newInstance()
            PERC_FRAGMENT_ID -> PercentageListFragment.newInstance()
            else -> CalculationFragment.newInstance()
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commitNow()
    }
}