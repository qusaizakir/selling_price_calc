package com.qzakapps.sellingpricecalc.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.airbnb.epoxy.EpoxyTouchHelper
import com.qzakapps.sellingpricecalc.R
import com.qzakapps.sellingpricecalc.activities.MainActivity
import com.qzakapps.sellingpricecalc.epoxy.controller.PercentageListEpoxyController
import com.qzakapps.sellingpricecalc.epoxy.model.PercentageListEpoxyModel
import com.qzakapps.sellingpricecalc.models.Percentage
import com.qzakapps.sellingpricecalc.viewmodels.PercentageListViewModel
import kotlinx.android.synthetic.main.cost_list_fragment.*
import kotlinx.android.synthetic.main.percentage_list_add_cost_dialog.view.*
import kotlinx.android.synthetic.main.percentage_list_fragment.*

class PercentageListFragment : BaseFragment<PercentageListViewModel>() {

    private lateinit var percentageListEpoxyController: PercentageListEpoxyController
    private lateinit var contextNotNull: Context

    companion object {
        fun newInstance() =
            PercentageListFragment()
    }

    override fun getViewModelClass(): Class<PercentageListViewModel> = PercentageListViewModel::class.java

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        return inflater.inflate(R.layout.percentage_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //setup
        setupRecyclerView()

        //inputs
        percentageListFragmentFAB.setOnClickListener { viewModel.inputs.fabClicked()}

        //outputs
        viewModel.outputs.percentageList.subscribe { percentageList -> percentageListEpoxyController.setPercentageList(percentageList)}.autoDispose()
        viewModel.outputs.openAddPercentageDialog().subscribe { openAddPercentageDialog() }.autoDispose()
        viewModel.outputs.percentageAdded().subscribe{ Toast.makeText(contextNotNull, getString(R.string.add_percentage_toast), Toast.LENGTH_SHORT).show()}.autoDispose()

    }

    private fun openAddPercentageDialog(){
        contextNotNull.let {
            MaterialDialog(it).run {
                customView(R.layout.percentage_list_add_cost_dialog, noVerticalPadding = true)
                noAutoDismiss()
                positiveButton (R.string.add) {
                    val nameEditText = getCustomView().percentageListAddPercentageDialogPercentageNameEt
                    val nameTextInput = getCustomView().percentageListAddPercentageDialogPercentageNameTi
                    val valueEditText = getCustomView().percentageListAddPercentageDialogPercentageValueEt
                    val valueTextInput = getCustomView().percentageListAddPercentageDialogPercentageValueTi

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
                            viewModel.inputs.addPercentageBtnClicked(Percentage(name = nameEditText.text.toString(), percentage = valueEditText.text.toString()))
                            dismiss()
                        }
                    }
                }
                negativeButton (R.string.cancel) { dismiss() }
            }.show()
        }
    }

    private fun setupRecyclerView() {
        percentageListEpoxyController = PercentageListEpoxyController(contextNotNull, viewModel.percentageCallback)
        percentageListRecyclerview.setController(percentageListEpoxyController)

        EpoxyTouchHelper
            .initSwiping(percentageListRecyclerview)
            .leftAndRight()
            .withTarget(PercentageListEpoxyModel::class.java)
            .andCallbacks(viewModel.percentageSwipeCallbacks)


        percentageListRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && costListFragmentFAB.visibility == View.VISIBLE) {
                    //costListFragmentFAB.startAnimation(AnimationUtils.loadAnimation(contextNotNull, R.anim.scale_down))
                    percentageListFragmentFAB.hide()
                } else if (dy < 0 && costListFragmentFAB.visibility != View.VISIBLE) {
                    //costListFragmentFAB.startAnimation(AnimationUtils.loadAnimation(contextNotNull, R.anim.scale_up))
                    percentageListFragmentFAB.show()
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