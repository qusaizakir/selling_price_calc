package com.qzakapps.sellingpricecalc.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

abstract class BaseFragment<V : ViewModel> : Fragment() {


    private val compositeDisposable = CompositeDisposable()
    protected lateinit var viewModel: V

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(getViewModelClass())
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    public fun Disposable.autoDispose() {
        compositeDisposable.add(this)
    }

    abstract fun getViewModelClass(): Class<V>
}