package com.qzakapps.sellingpricecalc.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.qzakapps.sellingpricecalc.R
import com.qzakapps.sellingpricecalc.activities.MainActivity
import com.qzakapps.sellingpricecalc.adapters.CalculationCostRecyclerAdapter
import com.qzakapps.sellingpricecalc.adapters.CalculationLoadTemplateRecyclerAdapter
import com.qzakapps.sellingpricecalc.adapters.CalculationPercentageRecyclerAdapter
import com.qzakapps.sellingpricecalc.epoxy.controller.CostListEpoxyController
import com.qzakapps.sellingpricecalc.helper.clearText
import com.qzakapps.sellingpricecalc.viewmodels.CalculationViewModel
import com.qzakapps.sellingpricecalc.viewmodels.CostListViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.calculation_fragment.*
import kotlinx.android.synthetic.main.calculation_load_dialog.view.*
import kotlinx.android.synthetic.main.calculation_save_dialog.view.*
import kotlinx.android.synthetic.main.cost_list_fragment.*

class CostListFragment : BaseFragment<CostListViewModel>() {

    private lateinit var costListEpoxyController: CostListEpoxyController
    private lateinit var contextNotNull: Context

    companion object {
        fun newInstance() =
            CostListFragment()
    }

    override fun getViewModelClass(): Class<CostListViewModel> = CostListViewModel::class.java

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        return inflater.inflate(R.layout.cost_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        costListEpoxyController = CostListEpoxyController(contextNotNull)
        costListRecyclerview.setControllerAndBuildModels(costListEpoxyController)

        viewModel.outputs.costList.subscribe { costList -> costListEpoxyController.setCostList(costList)}.autoDispose()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is MainActivity){
            contextNotNull = context
        }
    }
}