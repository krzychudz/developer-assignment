package com.example.developer_task.activities

import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.developer_task.R
import com.example.developer_task.adapters.ComicsAdapter
import com.example.developer_task.enums.DashboardScreenState
import com.example.developer_task.models.ComicsResponse
import com.example.developer_task.utils.PaginationListener
import com.example.developer_task.viewmodels.dashboard.DashboardViewModel
import com.jakewharton.rxbinding2.widget.textChanges
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit


class DashboardActivity : BaseActivity() {

    companion object {
        val SEARCH_DEBOUNCE_TIME_MS = 800
    }

    private val dashboardViewModel: DashboardViewModel by viewModel()
    private var comicsAdapter: ComicsAdapter? = null

    override fun getLayoutForActivity(): Int {
        return R.layout.activity_main
    }

    override fun onActivityReady() {
        initActivityView()
        initRecyclerView()
        initDataObservers()
        initSearchbar()
        dashboardViewModel.onViewReady()
    }

    private fun initActivityView() {
        supportActionBar?.hide()
    }

    private fun initDataObservers() {
        dashboardViewModel.comicsData.observe(this,
            androidx.lifecycle.Observer {
                it?.let {
                    handleComicsDataChange(it)
                }
            })
    }

    private fun handleComicsDataChange(comicsResponse: ComicsResponse) {
        dashboardViewModel.isDataLoading = false
        if (comicsAdapter == null) {
            initRecyclerView()
        }
        comicsAdapter?.removeProgressView()
        if (comicsResponse.data.results.isNotEmpty()) {
            if (comicsResponse.data.offset != 0) {
                dashboardViewModel.currentDataCount += comicsResponse.data.count
                comicsAdapter?.addAdapterData(dashboardViewModel.prepareViewModelWithComicsData(comicsResponse.data.results))
            } else {
                dashboardViewModel.resetPaginationInfo(comicsResponse)
                comicsAdapter?.setAdapterData(dashboardViewModel.prepareViewModelWithComicsData(comicsResponse.data.results))
            }
            toggleResultSection(DashboardScreenState.COMICS_FETCH_SUCCESS_STATE)
        } else {
            toggleResultSection(DashboardScreenState.COMICS_FETCH_FAILED_STATE)
        }
    }

    private fun initRecyclerView() {
        comicsAdapter = ComicsAdapter(this)
        comicsRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = comicsAdapter
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
            addOnScrollListener(object : PaginationListener(layoutManager as LinearLayoutManager, dashboardViewModel.pageSize) {
                override fun loadMoreItems() {
                    dashboardViewModel.isDataLoading = true
                    dashboardViewModel.currentDataOffset += dashboardViewModel.pageSize
                    comicsAdapter?.addProgressView()
                    if (serachbar.text.toString().isNotEmpty()) {
                        dashboardViewModel.getComicsList(dashboardViewModel.currentDataOffset.toString(), serachbar.text.toString())
                    } else {
                        dashboardViewModel.getComicsList(dashboardViewModel.currentDataOffset.toString(), null)
                    }
                }

                override fun isLastPage(): Boolean {
                    return dashboardViewModel.isLastDataPage()
                }

                override fun isLoading(): Boolean {
                   return dashboardViewModel.isDataLoading
                }

            })
        }
    }

    private fun initSearchbar() {
        serachbar.textChanges()
            .skip(1)
            .map{ it.toString() }
            .doOnNext { toggleResultSection(DashboardScreenState.COMICS_FETCHING_STATE) }
            .debounce(SEARCH_DEBOUNCE_TIME_MS.toLong(), TimeUnit.MILLISECONDS)
            .subscribe({
                if (it.isNotEmpty()) {
                    dashboardViewModel.getComicsList("0", it)
                } else {
                    dashboardViewModel.getComicsList("0", null)
                }
        }, {
            toggleResultSection(DashboardScreenState.COMICS_FETCH_FAILED_STATE)
        })
    }

    private fun toggleResultSection(dashboardScreenState: DashboardScreenState) {
        when(dashboardScreenState) {
            DashboardScreenState.COMICS_FETCHING_STATE -> {
                comicsRecyclerView.visibility = View.GONE
                noResultsSection.visibility = View.GONE
                progressBarSection.visibility = View.VISIBLE
            }
            DashboardScreenState.COMICS_FETCH_SUCCESS_STATE -> {
                comicsRecyclerView.visibility = View.VISIBLE
                noResultsSection.visibility = View.GONE
                progressBarSection.visibility = View.GONE
            }
            DashboardScreenState.COMICS_FETCH_FAILED_STATE -> {
                comicsRecyclerView.visibility = View.GONE
                noResultsSection.visibility = View.VISIBLE
                progressBarSection.visibility = View.GONE
            }
        }
    }
}

