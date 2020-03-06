package com.qzakapps.sellingpricecalc.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.qzakapps.sellingpricecalc.R
import com.qzakapps.sellingpricecalc.adapters.CalculationCostRecyclerAdapter
import com.qzakapps.sellingpricecalc.adapters.CalculationPercentageRecyclerAdapter
import com.qzakapps.sellingpricecalc.models.CostModel
import com.qzakapps.sellingpricecalc.models.PercentageModel
import com.qzakapps.sellingpricecalc.viewmodels.CalculationCostViewModel
import com.qzakapps.sellingpricecalc.viewmodels.CalculationPercentageViewModel
import kotlinx.android.synthetic.main.calculation_cost_fragment.*
import kotlinx.android.synthetic.main.calculation_percentage_fragment.*

class CalculationPercentageFragment : BaseFragment<CalculationPercentageViewModel>() {

    private lateinit var calculationPercentageRecyclerAdapter: CalculationPercentageRecyclerAdapter

    override fun getViewModelClass(): Class<CalculationPercentageViewModel> = CalculationPercentageViewModel::class.java

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.calculation_percentage_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        calculationPercentageRecyclerAdapter = CalculationPercentageRecyclerAdapter()

        calculationPercentageRecyclerView.apply {
            setHasFixedSize(true)
            adapter = calculationPercentageRecyclerAdapter
            layoutManager = LinearLayoutManager(this@CalculationPercentageFragment.context)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        calculationPercentageAddPercentageButton.setOnClickListener { viewModel.inputs.addPercentageModelBtnClick(PercentageModel("Perc String", "20.00"))}

        viewModel.outputs.percentageModelList().subscribe { percentageCostModel -> calculationPercentageRecyclerAdapter.setData(percentageCostModel) }.autoDispose()
    }
}