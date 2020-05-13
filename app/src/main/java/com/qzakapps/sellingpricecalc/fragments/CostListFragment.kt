package com.qzakapps.sellingpricecalc.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.airbnb.epoxy.EpoxyTouchHelper
import com.qzakapps.sellingpricecalc.R
import com.qzakapps.sellingpricecalc.activities.MainActivity
import com.qzakapps.sellingpricecalc.epoxy.controller.CostListEpoxyController
import com.qzakapps.sellingpricecalc.epoxy.model.CostListEpoxyModel
import com.qzakapps.sellingpricecalc.models.Cost
import com.qzakapps.sellingpricecalc.viewmodels.CostListViewModel
import kotlinx.android.synthetic.main.cost_list_add_cost_dialog.view.*
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

        //setup
        setupRecyclerView()

        //inputs
        costListFragmentFAB.setOnClickListener { viewModel.inputs.fabClicked()}

        //outputs
        viewModel.outputs.costList.subscribe { costList -> costListEpoxyController.setCostList(costList)}.autoDispose()
        viewModel.outputs.openAddCostDialog().subscribe { openAddCostDialog() }.autoDispose()
        viewModel.outputs.costAdded().subscribe{ Toast.makeText(contextNotNull, getString(R.string.cost_added), Toast.LENGTH_SHORT).show()}.autoDispose()

    }

    private fun openAddCostDialog(){
        contextNotNull.let {
            MaterialDialog(it).run {
                    customView(R.layout.cost_list_add_cost_dialog, noVerticalPadding = true)
                    noAutoDismiss()
                    positiveButton (R.string.add) {
                        val nameEditText = getCustomView().costListAddCostDialogCostNameEt
                        val nameTextInput = getCustomView().costListAddCostDialogCostNameTi
                        val valueEditText = getCustomView().costListAddCostDialogCostValueEt
                        val valueTextInput = getCustomView().costListAddCostDialogCostValueTi

                        when{
                            nameEditText.text.isNullOrEmpty() && valueEditText.text.isNullOrEmpty() ->{
                                nameTextInput.error = getString(R.string.empty_error)
                                valueTextInput.error = getString(R.string.empty_error)
                            }
                            !nameEditText.text.isNullOrEmpty() && valueEditText.text.isNullOrEmpty() -> {
                                valueTextInput.error = getString(R.string.empty_error)
                                nameTextInput.error = ""
                            }
                            nameEditText.text.isNullOrEmpty() && !valueEditText.text.isNullOrEmpty() -> {
                                nameTextInput.error = getString(R.string.empty_error)
                                valueTextInput.error = ""
                            }
                            else -> {
                                nameTextInput.error = ""
                                valueTextInput.error = ""
                                viewModel.inputs.addCostBtnClicked(Cost(name = nameEditText.text.toString(), cost = valueEditText.text.toString()))
                                dismiss()
                            }
                        }
                    }
                    negativeButton (R.string.cancel) { dismiss() }
                }.show()
        }
    }

    private fun setupRecyclerView() {
        costListEpoxyController = CostListEpoxyController(contextNotNull, viewModel.costCallback)
        costListRecyclerview.setController(costListEpoxyController)

        EpoxyTouchHelper
            .initSwiping(costListRecyclerview)
            .leftAndRight()
            .withTarget(CostListEpoxyModel::class.java)
            .andCallbacks(viewModel.costSwipeCallbacks)

        costListRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && costListFragmentFAB.visibility == View.VISIBLE) {
                    //costListFragmentFAB.startAnimation(AnimationUtils.loadAnimation(contextNotNull, R.anim.scale_down))
                    costListFragmentFAB.hide()
                } else if (dy < 0 && costListFragmentFAB.visibility != View.VISIBLE) {
                    //costListFragmentFAB.startAnimation(AnimationUtils.loadAnimation(contextNotNull, R.anim.scale_up))
                    costListFragmentFAB.show()
                }
            }
        })


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is MainActivity){
            contextNotNull = context
        }
    }
}