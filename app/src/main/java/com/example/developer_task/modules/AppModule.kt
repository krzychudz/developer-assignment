package com.example.developer_task.modules

import com.example.developer_task.schedulers.ApplicationSchedulerProvider
import com.example.developer_task.schedulers.SchedulerProvider
import com.example.developer_task.viewmodels.dashboard.DashboardViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val viewModelModules = module {
     viewModel { DashboardViewModel(get()) }
}

val rxModule = module {
    single<SchedulerProvider> { ApplicationSchedulerProvider() }
}

val viewModelsAndRxModules = listOf(viewModelModules, rxModule)