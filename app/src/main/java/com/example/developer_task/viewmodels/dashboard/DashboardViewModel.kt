package com.example.developer_task.viewmodels.dashboard

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.developer_task.backend.ApiClientInterface
import com.example.developer_task.models.ComicsData
import com.example.developer_task.models.ComicsModel
import com.example.developer_task.models.ComicsResponse
import com.example.developer_task.viewmodels.BaseViewModel
import com.example.developer_task.viewmodels.comics.ComicsViewModel

class DashboardViewModel(api: ApiClientInterface) : BaseViewModel(api) {
    val comicsData = MutableLiveData<ComicsResponse>()
    var isDataLoading = false
    var currentDataOffset = 0
    var allDataCount = 0
    var currentDataCount = 0
    var pageSize = 0

    override fun onViewReady() {
        getComicsList("0", null)
    }

    fun getComicsList(dataOffset: String, title: String?) {
        launch {
            api.getComicsData(dataOffset, title).subscribe({
                pageSize = it.data.limit
                comicsData.value = it
            }, {
                //comicsData.value = ComicsResponse(ComicsData())
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

    fun isLastDataPage(): Boolean {
        return allDataCount == currentDataCount
    }

    fun resetPaginationInfo(comicsResponse: ComicsResponse) {
        currentDataCount = comicsResponse.data.count
        currentDataOffset = 0
        allDataCount = comicsResponse.data.total
    }

}