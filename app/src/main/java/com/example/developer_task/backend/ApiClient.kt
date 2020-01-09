package com.example.developer_task.backend

import com.example.developer_task.schedulers.SchedulerProvider

class ApiClient(private val apiService: ApiService, private val schedulersProvider: SchedulerProvider) : ApiClientInterface {
//    override fun getUser(userId: Int): Single<ExampleUserModel> {
//        return apiService.getUser(userId)
//            .subscribeOn(schedulersProvider.io())
//            .observeOn(schedulersProvider.ui())
//    }
}