package com.example.developer_task.viewmodels.states

import com.example.developer_task.enums.DashboardScreenState
import com.example.developer_task.viewmodels.comics.ComicsViewModel

data class ComicsListState(val data: List<ComicsViewModel>?, val error: Throwable?, val state: DashboardScreenState) {
    companion object {
        fun ERROR_STATE(error: Throwable?) =
            ComicsListState(null, error, DashboardScreenState.COMICS_FETCH_FAILED_STATE)

        fun LOADING_STATE() =
            ComicsListState(null, null, DashboardScreenState.COMICS_FETCHING_STATE)

        fun SUCCESS_STATE(data: List<ComicsViewModel>?) =
            ComicsListState(data, null, DashboardScreenState.COMICS_FETCH_SUCCESS_STATE)
    }
}