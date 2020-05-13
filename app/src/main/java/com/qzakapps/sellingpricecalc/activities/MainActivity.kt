package com.qzakapps.sellingpricecalc.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.qzakapps.sellingpricecalc.R
import com.qzakapps.sellingpricecalc.fragments.CalculationFragment
import com.qzakapps.sellingpricecalc.fragments.CostListFragment
import com.qzakapps.sellingpricecalc.fragments.PercentageListFragment
import kotlinx.android.synthetic.main.main_activity.*

const val COST_FRAGMENT_ID = "COST_FRAGMENT_ID"
const val CALC_FRAGMENT_ID = "CALC_FRAGMENT_ID"
const val PERC_FRAGMENT_ID = "PERC_FRAGMENT_ID"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
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

    private fun goToFragment(id: String){
        val fragment = when(id){
            COST_FRAGMENT_ID -> {
                supportFragmentManager.findFragmentByTag(COST_FRAGMENT_ID) ?: CostListFragment.newInstance()
            }
            CALC_FRAGMENT_ID -> {
                supportFragmentManager.findFragmentByTag(CALC_FRAGMENT_ID) ?: CalculationFragment.newInstance()
            }
            PERC_FRAGMENT_ID -> {
                supportFragmentManager.findFragmentByTag(PERC_FRAGMENT_ID) ?: PercentageListFragment.newInstance()
            }
            else -> CalculationFragment.newInstance()
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment, id)
            .commitNow()
    }

    fun clickOnBottomMenuItem(item: String){
        when(item){
            COST_FRAGMENT_ID -> {
                bottom_navigation.selectedItemId = R.id.costListMenuItem
            }
            CALC_FRAGMENT_ID -> {
                bottom_navigation.selectedItemId = R.id.calculationMenuItem
            }
            PERC_FRAGMENT_ID -> {
                bottom_navigation.selectedItemId = R.id.percentageListMenuItem
            }
        }
    }
}