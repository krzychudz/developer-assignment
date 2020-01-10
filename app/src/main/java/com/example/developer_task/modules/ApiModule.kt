package com.example.developer_task.modules

import com.example.developer_task.backend.ApiClient
import com.example.developer_task.backend.ApiClientInterface
import com.example.developer_task.backend.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val BASE_URL = "https://gateway.marvel.com/"

val okHttpClientModule = module {
    single { createOkHttpClient() }
}

val apiServiceModule = module {
    single { createWebService<ApiService>(get(),  BASE_URL) }
}

val apiClientModule = module {
    single { ApiClient(get(), get()) as ApiClientInterface }
}

val startProjectApi = listOf(okHttpClientModule, apiServiceModule, apiClientModule) //TODO update my name

fun createOkHttpClient(): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
    val tmp =  OkHttpClient.Builder()
        .connectTimeout(60L, TimeUnit.SECONDS)
        .readTimeout(60L, TimeUnit.SECONDS)
        .addInterceptor(httpLoggingInterceptor).build()
    return tmp
}

inline fun <reified T> createWebService(okHttpClient: OkHttpClient, url: String): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    return retrofit.create(T::class.java)
}