package com.example.developer_task.viewmodels.dashboard

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.developer_task.backend.ApiClientInterface
import com.example.developer_task.models.ComicsModel
import com.example.developer_task.models.ComicsResponse
import com.example.developer_task.viewmodels.BaseViewModel
import com.example.developer_task.viewmodels.comics.ComicsViewModel

class DashboardViewModel(api: ApiClientInterface) : BaseViewModel(api) {
    val comicsData = MutableLiveData<ComicsResponse>()

    override fun onViewReady() {
        getComicsList("0", null)
    }

    fun getComicsList(dataOffset: String, title: String?) {
        launch {
            api.getComicsData(dataOffset, title).subscribe({
                comicsData.value = it
            }, { error ->
                error?.let {
                    it.message?.let { message ->
                        Log.e("test", message)
                    }
                }
            })
        }
    }

    fun prepareViewModelWithComicsData(comics: List<ComicsModel>) : List<ComicsViewModel> {
        val comicsViewModelList = ArrayList<ComicsViewModel>()
        comics.forEach { comics ->
            comicsViewModelList.add(ComicsViewModel(comics))
        }
        return comicsViewModelList
    }

}