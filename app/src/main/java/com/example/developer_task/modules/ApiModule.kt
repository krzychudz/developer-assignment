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

class ApiModule {

    private val okHttpClientModule = module {
        single { createOkHttpClient() }
    }

    private val apiServiceModule = module {
        single { createWebService<ApiService>(get(), "test") } //TODO Check that later
    }

    private val apiClientModule = module {
        single { ApiClient(get(), get()) as ApiClientInterface }
    }

    val apiModules = listOf(okHttpClientModule, apiServiceModule, apiClientModule)

    private fun createOkHttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
        return OkHttpClient.Builder()
            .connectTimeout(60L, TimeUnit.SECONDS)
            .readTimeout(60L, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor).build()
    }

    private inline fun <reified T> createWebService(okHttpClient: OkHttpClient, url: String): T {
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
        return retrofit.create(T::class.java)
    }

}