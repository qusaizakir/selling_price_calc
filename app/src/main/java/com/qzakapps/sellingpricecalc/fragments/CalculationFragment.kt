package com.qzakapps.sellingpricecalc.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.qzakapps.sellingpricecalc.R
import com.qzakapps.sellingpricecalc.adapters.CalculationCostRecyclerAdapter
import com.qzakapps.sellingpricecalc.adapters.CalculationLoadTemplateRecyclerAdapter
import com.qzakapps.sellingpricecalc.adapters.CalculationPercentageRecyclerAdapter
import com.qzakapps.sellingpricecalc.helper.clearText
import com.qzakapps.sellingpricecalc.viewmodels.CalculationViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.calculation_fragment.*
import kotlinx.android.synthetic.main.calculation_load_dialog.view.*
import kotlinx.android.synthetic.main.calculation_save_dialog.view.*

class CalculationFragment : BaseFragment<CalculationViewModel>() {

    private lateinit var loadTemplateDialogAdapter: CalculationLoadTemplateRecyclerAdapter
    private var costAdapter: CalculationCostRecyclerAdapter = CalculationCostRecyclerAdapter()
    private var percentageAdapter: CalculationPercentageRecyclerAdapter = CalculationPercentageRecyclerAdapter()
    private var loadDialog: MaterialDialog? = null
    private var saveDialog: MaterialDialog? = null

    companion object {
        fun newInstance() =
            CalculationFragment()
    }

    override fun getViewModelClass(): Class<CalculationViewModel> = CalculationViewModel::class.java

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.calculation_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        calculationCostListRV.adapter = costAdapter
        calculationPercentageListRV.adapter = percentageAdapter
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.calculation_toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.calculation_menu_save_template -> viewModel.onSaveTemplateBtnClicked()
            R.id.calculation_menu_load_template -> viewModel.onLoadTemplateBtnClicked()
            R.id.calculation_menu_settings -> viewModel.onSettingsBtnClicked()
            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //Loading template adapter
        loadTemplateDialogAdapter = CalculationLoadTemplateRecyclerAdapter(templateClickedInterface = viewModel)

        //Create Dialogs
        createLoadDialog()
        createSaveDialog()

        //Load Template (returns a single, so will only execute once)
        viewModel.loadTemplate().subscribeOn(Schedulers.io()).subscribe { template -> viewModel.templateClicked(template, false) }.autoDispose()

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

        viewModel.outputs.costList.subscribe { costList -> costAdapter.setData(costList)}.autoDispose()
        viewModel.outputs.percentageList.subscribe{ percentageList -> percentageAdapter.setData(percentageList)}.autoDispose()

        viewModel.outputs.clearCostNameAndCost.subscribe {
            calculationCostNameEt.clearText(); calculationCostNameEt.clearFocus()
            calculationCostValueEt.clearText(); calculationCostValueEt.clearFocus()
        }.autoDispose()
        viewModel.outputs.clearPercentageNameAndCost.subscribe{
            calculationPercentageNameEt.clearText(); calculationPercentageNameEt.clearFocus()
            calculationPercentageValueEt.clearText(); calculationPercentageValueEt.clearFocus()
        }.autoDispose()

        viewModel.outputs.salePriceOutput.subscribe { t -> calculationSalePriceEt.setText(t)}.autoDispose()
        viewModel.outputs.markupOutput.subscribe { t -> calculationMarkupEt.setText(t)}.autoDispose()
        viewModel.outputs.profitOutput.subscribe { t -> calculationProfitEt.setText(t)}.autoDispose()
        viewModel.outputs.profitMarginOutput.subscribe { t -> calculationProfitMarginEt.setText(t)}.autoDispose()

        viewModel.outputs.displayFixedCost().subscribe { t -> calculationCostTotalTv.text = t}.autoDispose()
        viewModel.outputs.displayPercentageCost().subscribe { t -> calculationPercentageTotalTv.text = getString(R.string.percentage_format, t) }.autoDispose()

        viewModel.outputs.profitMarginError.subscribe{showError -> calculationProfitMarginTi.error = if(showError) getString(R.string.profit_margin_error) else "" }.autoDispose()

        viewModel.outputs.saveTemplate().subscribe{ Toast.makeText(context, getString(R.string.saved_toast), Toast.LENGTH_SHORT).show()}.autoDispose()

        viewModel.outputs.showSaveDialog.subscribe { saveDialog?.show() }.autoDispose()
        viewModel.outputs.showLoadDialog.subscribe { loadDialog?.show() }.autoDispose()

        viewModel.outputs.templateList.observeOn(AndroidSchedulers.mainThread()).subscribe{ templateList -> loadTemplateDialogAdapter.setData(templateList)}.autoDispose()
        viewModel.outputs.templateLoaded.subscribe{
            loadDialog?.dismiss()
            Toast.makeText(context, "Loaded Template", Toast.LENGTH_SHORT).show()
        }.autoDispose()
        viewModel.changeTemplateName.subscribe {t ->  calculationTemplateTitleTv.text = t }.autoDispose()
        //endregion

    }

    override fun onPause() {
        viewModel.saveCurrentTemplateOnClose()
        super.onPause()
    }

    private fun createSaveDialog() {
        context?.let {
            saveDialog =
                MaterialDialog(it)
                    .run {
                        customView(R.layout.calculation_save_dialog, noVerticalPadding = true)
                        noAutoDismiss()
                        positiveButton (R.string.save) {
                            val nameEditText = getCustomView().calculationSaveDialogTemplateNameEt
                            val nameTextInput = getCustomView().calculationSaveDialogTemplateNameTi

                            if (!nameEditText.text.isNullOrEmpty()){
                                viewModel.saveTemplateBtnClicked.onNext(nameEditText.text.toString())
                                nameTextInput.error = ""
                                dismiss()
                            }else{
                                nameTextInput.error = getString(R.string.template_name_error)
                            }
                        }
                        negativeButton (R.string.cancel) { dismiss() }
                    }
        }
    }

    private fun createLoadDialog() {
        context?.let {
            loadDialog = MaterialDialog(it)
                            .run {
                                customView(R.layout.calculation_load_dialog, noVerticalPadding = true)
                                negativeButton (R.string.cancel) { dismiss() }
                            }
            val recyclerView = loadDialog?.getCustomView()?.calculationLoadDialogRecyclerView
            recyclerView?.adapter = loadTemplateDialogAdapter
        }
    }

}