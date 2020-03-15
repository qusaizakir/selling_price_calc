package com.qzakapps.sellingpricecalc.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

        calculationCostNameEt.doAfterTextChanged { text -> viewModel.onCostNameTextChange(text.toString())}
        calculationCostValueEt.doAfterTextChanged { text -> viewModel.onCostValueTextChange(text.toString())}
        calculationPercentageNameEt.doAfterTextChanged { text -> viewModel.onPercentageNameTextChange(text.toString())}
        calculationPercentageValueEt.doAfterTextChanged { text -> viewModel.onPercentageValueTextChange(text.toString())}

        calculationAddCostBtn.setOnClickListener { viewModel.onAddCostBtnClicked()}
        calculationAddPercentageBtn.setOnClickListener { viewModel.onAddPercentageBtnClicked()}

        calculationProfitMarginEt.doAfterTextChanged { text -> if(calculationProfitMarginEt.hasFocus()) viewModel.onProfitMarginTextChange(text.toString())}
        calculationSalePriceEt.doAfterTextChanged { text -> if(calculationSalePriceEt.hasFocus()) viewModel.onSalePriceTextChange(text.toString())}
        calculationMarkupEt.doAfterTextChanged { text -> if(calculationMarkupEt.hasFocus()) viewModel.onMarkupTextChange(text.toString())}
        calculationProfitEt.doAfterTextChanged { text -> if(calculationProfitEt.hasFocus())  viewModel.onProfitTextChange(text.toString())}

        viewModel.outputs.costBtnEnabled().subscribe {enabled -> calculationAddCostBtn.isEnabled = enabled}.autoDispose()
        viewModel.outputs.percentageBtnEnabled().subscribe {enabled -> calculationAddPercentageBtn.isEnabled = enabled}.autoDispose()

        viewModel.outputs.insertCost().subscribe { Toast.makeText(context, "Cost Added", Toast.LENGTH_SHORT).show()}.autoDispose()
        viewModel.outputs.insertPercentage().subscribe { Toast.makeText(context, "Percentage Added", Toast.LENGTH_SHORT).show()}.autoDispose()

        viewModel.outputs.salePrice().subscribe { t -> calculationSalePriceEt.setText(t)}.autoDispose()
        viewModel.outputs.markup().subscribe { t -> calculationMarkupEt.setText(t)}.autoDispose()
        viewModel.outputs.profit().subscribe { t -> calculationProfitEt.setText(t)}.autoDispose()
        viewModel.outputs.profitMargin().subscribe { t -> calculationProfitMarginEt.setText(t)}.autoDispose()

        viewModel.outputs.profitMarginError.subscribe{showError -> calculationProfitMarginTi.error = if(showError) getString(R.string.profit_margin_error) else "" }.autoDispose()

    }



}