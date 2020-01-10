package com.example.developer_task.backend

import com.example.developer_task.models.ComicsResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/v1/public/comics?ts=1&limit=25&orderBy=-onsaleDate")
    fun getComicsData(@Query("offset") offset: String,
                      @Query("title") title: String?
    ): Single<ComicsResponse>
}