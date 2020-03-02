package com.qzakapps.sellingpricecalc.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.qzakapps.sellingpricecalc.R
import com.qzakapps.sellingpricecalc.adapters.CalculationCostRecyclerAdapter
import com.qzakapps.sellingpricecalc.models.CostModel
import com.qzakapps.sellingpricecalc.viewmodels.CalculationCostViewModel
import kotlinx.android.synthetic.main.calculation_cost_fragment.*

class CalculationCostFragment : BaseFragment<CalculationCostViewModel>() {

    private lateinit var calculationCostRecyclerAdapter: CalculationCostRecyclerAdapter

    override fun getViewModelClass(): Class<CalculationCostViewModel> = CalculationCostViewModel::class.java

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.calculation_cost_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        calculationCostRecyclerAdapter = CalculationCostRecyclerAdapter()

        calculationCostRecyclerView.apply {
            setHasFixedSize(true)
            adapter = calculationCostRecyclerAdapter
            layoutManager = LinearLayoutManager(this@CalculationCostFragment.context)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        calculationCostAddCostButton.setOnClickListener { viewModel.inputs.addCostModelBtnClick(CostModel("Test String", "20.00")) }

        viewModel.outputs.costModelList().subscribe { costModelList -> calculationCostRecyclerAdapter.setData(costModelList) }.autoDispose()
    }
}
