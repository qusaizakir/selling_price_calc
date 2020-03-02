package com.qzakapps.sellingpricecalc.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.tabs.TabLayoutMediator
import com.qzakapps.sellingpricecalc.R
import com.qzakapps.sellingpricecalc.adapters.CalculationViewPagerAdapter
import com.qzakapps.sellingpricecalc.viewmodels.CalculationViewModel
import kotlinx.android.synthetic.main.calculation_fragment.*

class CalculationFragment() : BaseFragment<CalculationViewModel>() {

    private lateinit var calculationViewPagerAdapter: CalculationViewPagerAdapter

    companion object {
        fun newInstance() =
            CalculationFragment()
    }

    override fun getViewModelClass(): Class<CalculationViewModel> = CalculationViewModel::class.java

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        return inflater.inflate(R.layout.calculation_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        calculationViewPagerAdapter = CalculationViewPagerAdapter(this)
        calculationPager.adapter = calculationViewPagerAdapter
        TabLayoutMediator(calculationTabLayout, calculationPager){
            tab, position -> tab.text = if(position == 0) "Cost Price" else "Percentage Fee"
            //TODO fix the name of the tabs
        }.attach()

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //Take as list from the viewpager

        //calculationEtFixedCost.doAfterTextChanged { text -> if(calculationEtFixedCost.hasFocus()) viewModel.onFixedCostTextChange(text.toString())}
        //calculationEtPercentageCost.doAfterTextChanged { text -> if(calculationEtPercentageCost.hasFocus()) viewModel.onPercentCostTextChange(text.toString())}

        calculationEtProfitMargin.doAfterTextChanged { text -> if(calculationEtProfitMargin.hasFocus()) viewModel.onProfitMarginTextChange(text.toString())}
        calculationEtSalePrice.doAfterTextChanged { text -> if(calculationEtSalePrice.hasFocus()) viewModel.onSalePriceTextChange(text.toString())}
        calculationEtMarkup.doAfterTextChanged { text -> if(calculationEtMarkup.hasFocus()) viewModel.onMarkupTextChange(text.toString())}
        calculationEtProfit.doAfterTextChanged { text -> if(calculationEtProfit.hasFocus())  viewModel.onProfitTextChange(text.toString())}

        viewModel.outputs.salePrice().subscribe { t -> calculationEtSalePrice.setText(t)}.autoDispose()
        viewModel.outputs.markup().subscribe { t -> calculationEtMarkup.setText(t)}.autoDispose()
        viewModel.outputs.profit().subscribe { t -> calculationEtProfit.setText(t)}.autoDispose()
        viewModel.outputs.profitMargin().subscribe { t -> calculationEtProfitMargin.setText(t)}.autoDispose()

        viewModel.outputs.profitMarginError.subscribe{showError -> calculationTiProfitMargin.error = if(showError) getString(R.string.profit_margin_error) else "" }

    }



}