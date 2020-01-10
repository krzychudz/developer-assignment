package com.example.developer_task.backend

import com.example.developer_task.models.ComicsResponse
import com.example.developer_task.schedulers.SchedulerProvider
import io.reactivex.Single

class ApiClient(private val apiService: ApiService, private val schedulersProvider: SchedulerProvider) : ApiClientInterface {
    override fun getComicsData(dataOffset: String, title: String?): Single<ComicsResponse> {
       return apiService.getComicsData(dataOffset, title)
           .subscribeOn(schedulersProvider.io())
           .observeOn(schedulersProvider.ui())
    }
}