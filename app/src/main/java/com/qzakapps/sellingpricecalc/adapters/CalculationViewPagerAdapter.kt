package com.qzakapps.sellingpricecalc.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.qzakapps.sellingpricecalc.fragments.CalculationCostFragment
import com.qzakapps.sellingpricecalc.fragments.CalculationPercentageFragment

class CalculationViewPagerAdapter (fragment: Fragment): FragmentStateAdapter(fragment){

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if(position == 0) CalculationCostFragment() else CalculationPercentageFragment()
    }

}