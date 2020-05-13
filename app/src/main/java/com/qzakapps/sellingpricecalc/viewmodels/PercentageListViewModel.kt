package com.qzakapps.sellingpricecalc.viewmodels

import android.app.Application
import android.graphics.Canvas
import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import com.airbnb.epoxy.EpoxyTouchHelper.SwipeCallbacks
import com.qzakapps.sellingpricecalc.R
import com.qzakapps.sellingpricecalc.epoxy.model.PercentageListEpoxyModel
import com.qzakapps.sellingpricecalc.models.Percentage
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import kotlin.math.abs

interface PercentageListInputs {
    fun fabClicked()
    fun addPercentageBtnClicked(percentage: Percentage)
}

interface PercentageListOutputs {
    fun openAddPercentageDialog(): Observable<Unit>
    fun percentageAdded(): Observable<Unit>
    val percentageList: Observable<List<Percentage>>
}

class PercentageListViewModel(application: Application) :
    BaseViewModel(application),
    PercentageListInputs,
    PercentageListOutputs,
    PercentageClickedCallBack{

    val outputs: PercentageListOutputs = this
    val inputs: PercentageListInputs = this
    val percentageCallback: PercentageClickedCallBack = this
    val percentageSwipeCallbacks: PercentageSwipeCallback = PercentageSwipeCallback()

    private val fabClickedPs: PublishSubject<Unit> = PublishSubject.create()
    private val addPercentageBtnClickedPs: PublishSubject<Percentage> = PublishSubject.create()

    override val percentageList: Observable<List<Percentage>> = repo.getAllPercentage

    //region inputs
    override fun fabClicked() {
        fabClickedPs.onNext(Unit)
    }

    override fun addPercentageBtnClicked(percentage: Percentage) {
        addPercentageBtnClickedPs.onNext(percentage)
    }

    override fun onPercentageClicked(id: String) {
        repo.toggleActivePercentage(id)
    }
    //endregion

    //region outputs
    override fun openAddPercentageDialog(): Observable<Unit> {
        return fabClickedPs.map { Unit }
    }

    override fun percentageAdded(): Observable<Unit> {
        return addPercentageBtnClickedPs.map { percentage -> repo.insertPercentage(percentage)}.observeOn(AndroidSchedulers.mainThread())
    }
    //endregion

    //region swipe callbacks class
    inner class PercentageSwipeCallback: SwipeCallbacks<PercentageListEpoxyModel>(){
        override fun onSwipeCompleted(model: PercentageListEpoxyModel?, itemView: View?, position: Int, direction: Int){
            (itemView?.findViewById<LinearLayout>(R.id.percentageListRecyclerviewLL))?.setBackgroundColor(Color.argb(0, 191, 4,4))
            model?.percentageId?.let { repo.deletePercentageById(it) }
        }

        override fun onSwipeStarted(model: PercentageListEpoxyModel?, itemView: View?, adapterPosition: Int){
            (itemView?.findViewById<LinearLayout>(R.id.percentageListRecyclerviewLL))?.setBackgroundColor(Color.argb(255, 191, 4,4))
        }

        override fun onSwipeProgressChanged(model: PercentageListEpoxyModel?, itemView: View?, swipeProgress: Float, canvas: Canvas?){
            val alpha = abs(255 * (swipeProgress)).toInt()
            (itemView?.findViewById<LinearLayout>(R.id.percentageListRecyclerviewLL))?.setBackgroundColor(Color.argb(alpha, 191, 4,4))
        }

        override fun onSwipeReleased(model: PercentageListEpoxyModel?, itemView: View?) {
            (itemView?.findViewById<LinearLayout>(R.id.percentageListRecyclerviewLL))?.setBackgroundColor(Color.argb(0, 191, 4,4))
        }
    }
    //endregion

}

interface PercentageClickedCallBack {
    fun onPercentageClicked(id: String)
}



