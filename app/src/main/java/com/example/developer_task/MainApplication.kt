package com.example.developer_task

import android.app.Application
import com.example.developer_task.modules.startProjectApi
import com.example.developer_task.modules.viewModelsAndRxModules
import org.koin.android.ext.android.startKoin

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this,  startProjectApi + viewModelsAndRxModules)
    }
}