package com.qzakapps.sellingpricecalc.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import com.qzakapps.sellingpricecalc.R
import com.qzakapps.sellingpricecalc.viewmodels.CalculationViewModel
import kotlinx.android.synthetic.main.calculation_fragment.*

class CalculationFragment() : BaseFragment<CalculationViewModel>() {

    companion object {
        fun newInstance() =
            CalculationFragment()
    }
    override fun getViewModelClass(): Class<CalculationViewModel> = CalculationViewModel::class.java

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        return inflater.inflate(R.layout.calculation_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        calculationEtFixedCost.doAfterTextChanged { text -> if(calculationEtFixedCost.hasFocus()) viewModel.onFixedCostTextChange(text.toString())}
        calculationEtPercentageCost.doAfterTextChanged { text -> if(calculationEtPercentageCost.hasFocus()) viewModel.onPercentCostTextChange(text.toString())}

        calculationEtProfitMargin.doAfterTextChanged { text -> if(calculationEtProfitMargin.hasFocus()) viewModel.onProfitMarginTextChange(text.toString())}
        calculationEtSalePrice.doAfterTextChanged { text -> if(calculationEtSalePrice.hasFocus()) viewModel.onSalePriceTextChange(text.toString())}
        calculationEtProfit.doAfterTextChanged { text -> if(calculationEtProfit.hasFocus())  viewModel.onProfitTextChange(text.toString())}
        calculationEtMarkup.doAfterTextChanged { text -> if(calculationEtMarkup.hasFocus()) viewModel.onMarkupTextChange(text.toString())}

        viewModel.outputSalePrice.subscribe {t -> calculationEtSalePrice.setText(t)}.autoDispose()
        viewModel.outputMarkup.subscribe {t -> calculationEtMarkup.setText(t)}.autoDispose()
        viewModel.outputProfit.subscribe {t -> calculationEtProfit.setText(t)}.autoDispose()
        viewModel.outputProfitMargin.subscribe {t -> calculationEtProfitMargin.setText(t)}.autoDispose()
    }



}