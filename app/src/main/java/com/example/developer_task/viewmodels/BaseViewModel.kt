package com.example.developer_task.viewmodels

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import com.example.developer_task.backend.ApiClientInterface
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel(val api: ApiClientInterface) : ViewModel() {

    abstract fun onViewReady()

    val disposables = CompositeDisposable()

    fun launch(job: () -> Disposable) {
        disposables.add(job())
    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}