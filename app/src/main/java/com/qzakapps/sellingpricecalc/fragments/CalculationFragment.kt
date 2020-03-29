package com.qzakapps.sellingpricecalc.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.google.android.material.tabs.TabLayoutMediator
import com.qzakapps.sellingpricecalc.R
import com.qzakapps.sellingpricecalc.adapters.CalculationViewPagerAdapter
import com.qzakapps.sellingpricecalc.helper.clearText
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
            tab, position -> tab.text = if(position == 0) getString(R.string.cost_tab_title) else getString(R.string.percentage_tab_title)
        }.attach()

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //region inputs
        calculationCostNameEt.doAfterTextChanged { text -> viewModel.onCostNameTextChange(text.toString())}
        calculationCostValueEt.doAfterTextChanged { text -> viewModel.onCostValueTextChange(text.toString())}
        calculationPercentageNameEt.doAfterTextChanged { text -> viewModel.onPercentageNameTextChange(text.toString())}
        calculationPercentageValueEt.doAfterTextChanged { text -> viewModel.onPercentageValueTextChange(text.toString())}

        calculationAddCostBtn.setOnClickListener { viewModel.onAddCostBtnClicked()}
        calculationAddPercentageBtn.setOnClickListener { viewModel.onAddPercentageBtnClicked()}
        calculationSaveBtn.setOnClickListener {viewModel.onSaveTemplateBtnClicked()}

        calculationProfitMarginEt.doAfterTextChanged { text ->
            if(calculationProfitMarginEt.hasFocus()) { viewModel.onProfitMarginTextChangeByUser(text.toString()) }
            viewModel.onProfitMarginStringChanged(text.toString())
        }
        calculationSalePriceEt.doAfterTextChanged { text ->
            if(calculationSalePriceEt.hasFocus()) { viewModel.onSalePriceTextChangeByUser(text.toString()) }
            viewModel.onSalePriceStringChanged(text.toString())
        }
        calculationMarkupEt.doAfterTextChanged { text ->
            if(calculationMarkupEt.hasFocus()) { viewModel.onMarkupTextChangeByUser(text.toString()) }
            viewModel.onMarkupStringChanged(text.toString())
        }
        calculationProfitEt.doAfterTextChanged { text ->
            if(calculationProfitEt.hasFocus()) { viewModel.onProfitTextChangeByUser(text.toString()) }
            viewModel.onProfitStringChanged(text.toString())
        }
        //endregion

        //region outputs
        viewModel.outputs.costBtnEnabled().subscribe {enabled -> calculationAddCostBtn.isEnabled = enabled}.autoDispose()
        viewModel.outputs.percentageBtnEnabled().subscribe {enabled -> calculationAddPercentageBtn.isEnabled = enabled}.autoDispose()

        viewModel.outputs.insertCost().subscribe { Toast.makeText(context, R.string.add_cost_toast, Toast.LENGTH_SHORT).show()}.autoDispose()
        viewModel.outputs.insertPercentage().subscribe { Toast.makeText(context, R.string.add_percentage_toast, Toast.LENGTH_SHORT).show()}.autoDispose()

        viewModel.outputs.clearCostNameAndCost.subscribe {
            calculationCostNameEt.clearText(); calculationCostNameEt.clearFocus()
            calculationCostValueEt.clearText(); calculationCostValueEt.clearFocus()
        }.autoDispose()
        viewModel.outputs.clearPercentageNameAndCost.subscribe{
            calculationPercentageNameEt.clearText(); calculationPercentageNameEt.clearFocus()
            calculationPercentageValueEt.clearText(); calculationPercentageValueEt.clearFocus()
        }.autoDispose()

        viewModel.outputs.salePrice().subscribe { t -> calculationSalePriceEt.setText(t)}.autoDispose()
        viewModel.outputs.markup().subscribe { t -> calculationMarkupEt.setText(t)}.autoDispose()
        viewModel.outputs.profit().subscribe { t -> calculationProfitEt.setText(t)}.autoDispose()
        viewModel.outputs.profitMargin().subscribe { t -> calculationProfitMarginEt.setText(t)}.autoDispose()

        viewModel.outputs.displayFixedCost().subscribe { t -> calculationCostTotalTv.text = t}.autoDispose()
        viewModel.outputs.displayPercentageCost().subscribe { t -> calculationPercentageTotalTv.text = getString(R.string.percentage_format, t) }.autoDispose()

        viewModel.outputs.profitMarginError.subscribe{showError -> calculationProfitMarginTi.error = if(showError) getString(R.string.profit_margin_error) else "" }.autoDispose()

        viewModel.outputs.saveTemplate().subscribe{ Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()}.autoDispose()

        viewModel.outputs.showSaveDialog.subscribe { showSaveDialog() }.autoDispose()

        //endregion

    }

    private fun showSaveDialog(){
        context?.let {
            MaterialDialog(it).show {
                customView(R.layout.calculation_save_dialog, noVerticalPadding = true)
                positiveButton (R.string.save) { viewModel.saveTemplateBtnClicked.onNext(true) }
                negativeButton (R.string.cancel) { dismiss() }
            }
        }
    }

}