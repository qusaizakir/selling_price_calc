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

        calculationEtCostPrice.doAfterTextChanged { text -> viewModel.onCostPriceTextChange(text.toString())}
        calculationEtProfitMargin.doAfterTextChanged { text -> viewModel.onProfitMarginTextChange(text.toString())}

        viewModel.outputSalePrice.subscribe {t -> calculationEtSalePrice.setText(t)}.autoDispose()
    }



}