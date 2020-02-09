package com.example.developer_task.viewmodels.dashboard

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.developer_task.backend.ApiClientInterface
import com.example.developer_task.models.ComicsData
import com.example.developer_task.models.ComicsModel
import com.example.developer_task.models.ComicsResponse
import com.example.developer_task.viewmodels.BaseViewModel
import com.example.developer_task.viewmodels.comics.ComicsViewModel
import com.example.developer_task.viewmodels.states.ComicsListState

class DashboardViewModel(api: ApiClientInterface) : BaseViewModel(api) {
    val comicsData = MutableLiveData<ComicsListState>()
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
            api.getComicsData(dataOffset, title).retry().subscribe({
                handleComicsResponse(it)
            }, {
                comicsData.value = ComicsListState.ERROR_STATE(it)
            })
        }
    }

    fun setSearchingState() {
        comicsData.value = ComicsListState.LOADING_STATE()
    }

    private fun handleComicsResponse(comicsResponse: ComicsResponse) {
        if (comicsResponse.data.offset != 0) {
            currentDataCount += comicsResponse.data.count
        } else {
            resetPaginationInfo(comicsResponse)
        }
        pageSize = comicsResponse.data.limit
        if (comicsResponse.data.results.isNotEmpty()) {
            comicsData.value =
                ComicsListState.SUCCESS_STATE(prepareViewModelWithComicsData(comicsResponse.data.results))
        } else {
            comicsData.value =
                ComicsListState.ERROR_STATE(Throwable("Sorry, no results found"))
        }
    }

    private fun prepareViewModelWithComicsData(comics: List<ComicsModel>) : List<ComicsViewModel> {
        val comicsViewModelList = ArrayList<ComicsViewModel>()
        comics.forEach { comics ->
            comicsViewModelList.add(ComicsViewModel(comics))
        }
        return comicsViewModelList
    }

    fun isLastDataPage(): Boolean {
        return allDataCount == currentDataCount
    }

    private fun resetPaginationInfo(comicsResponse: ComicsResponse) {
        currentDataCount = comicsResponse.data.count
        currentDataOffset = 0
        allDataCount = comicsResponse.data.total
    }

}