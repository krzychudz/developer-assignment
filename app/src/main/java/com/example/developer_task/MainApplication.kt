package com.example.developer_task

import android.app.Application
import com.example.developer_task.modules.apiServicesModule
import com.example.developer_task.modules.viewModelsAndRxModules
import org.koin.android.ext.android.startKoin

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this,  apiServicesModule + viewModelsAndRxModules)
    }
}