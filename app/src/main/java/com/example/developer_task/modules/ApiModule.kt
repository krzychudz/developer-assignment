package com.example.developer_task.modules

import com.example.developer_task.BuildConfig
import com.example.developer_task.backend.ApiClient
import com.example.developer_task.backend.ApiClientInterface
import com.example.developer_task.backend.ApiService
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

val okHttpClientModule = module {
    single { createOkHttpClient() }
}

val apiServiceModule = module {
    single { createWebService<ApiService>(get(),  BuildConfig.BASE_URL) }
}

val apiClientModule = module {
    single { ApiClient(get(), get()) as ApiClientInterface }
}

val apiServicesModule = listOf(okHttpClientModule, apiServiceModule, apiClientModule)

fun createOkHttpClient(): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
    return OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .connectTimeout(60L, TimeUnit.SECONDS)
        .readTimeout(60L, TimeUnit.SECONDS)
        .addInterceptor(httpLoggingInterceptor).build()
}

internal class AuthInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val originalHttpUrl: HttpUrl = request.url
        val url = originalHttpUrl.newBuilder()
            .addQueryParameter("apikey", BuildConfig.API_KEY)
            .addQueryParameter("hash", BuildConfig.HASH)
            .build()
        val requestBuilder: Request.Builder = request.newBuilder()
            .url(url)
        val finalRequest: Request = requestBuilder.build()
        return chain.proceed(finalRequest)
    }
}

inline fun <reified T> createWebService(okHttpClient: OkHttpClient, url: String): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    return retrofit.create(T::class.java)
}