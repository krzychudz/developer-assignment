package com.example.developer_task.activities

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.developer_task.R
import com.example.developer_task.adapters.ComicsAdapter
import com.example.developer_task.utils.PaginationListener
import com.example.developer_task.viewmodels.dashboard.DashboardViewModel
import com.jakewharton.rxbinding2.widget.textChanges
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit


class DashboardActivity : BaseActivity() {

    private val dashboardViewModel: DashboardViewModel by viewModel()
    private var comicsAdapter: ComicsAdapter? = null

    private var isDataLoading = false
    private var currentDataOffset = 0
    private var allDataCount = 0
    private var currentDataCount = 0

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
                    isDataLoading = false
                    if (comicsAdapter == null) {
                        initRecyclerView()
                    }
                    comicsAdapter?.removeProgressView()
                    if (it.data.offset != 0) {
                        currentDataCount += it.data.count
                        comicsAdapter?.addAdapterData(dashboardViewModel.prepareViewModelWithComicsData(it.data.results))
                    } else {
                        currentDataCount = it.data.count
                        currentDataOffset = 0
                        allDataCount = it.data.total
                        comicsAdapter?.setAdapterData(dashboardViewModel.prepareViewModelWithComicsData(it.data.results))
                    }
                    toggleResultSection(true)
                }
            })
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
            addOnScrollListener(object : PaginationListener(layoutManager as LinearLayoutManager) {
                override fun loadMoreItems() {
                    isDataLoading = true
                    currentDataOffset += PAGE_SIZE
                    comicsAdapter?.addProgressView()
                    if (serachbar.text.toString().isNotEmpty()) {
                        dashboardViewModel.getComicsList(currentDataOffset.toString(), serachbar.text.toString())
                    } else {
                        dashboardViewModel.getComicsList(currentDataOffset.toString(), null)
                    }
                }

                override fun isLastPage(): Boolean {
                    return allDataCount == currentDataCount
                }

                override fun isLoading(): Boolean {
                   return isDataLoading
                }

            })
        }
    }

    private fun initSearchbar() {
        serachbar.textChanges()
            .skip(1)
            .map{ it.toString() }
            .doOnNext { toggleResultSection(false) }
            .debounce(800, TimeUnit.MILLISECONDS)
            .subscribe({
                if (it.isNotEmpty()) {
                    dashboardViewModel.getComicsList("0", it)
                } else {
                    dashboardViewModel.getComicsList("0", null)
                }
        }, {
            Log.e("error", it.message)
        })
    }

    private fun toggleResultSection(isResultAvailable: Boolean) {
        if (isResultAvailable) {
            comicsRecyclerView.visibility = View.VISIBLE
            progressBarSection.visibility = View.GONE
        } else {
            comicsRecyclerView.visibility = View.GONE
            progressBarSection.visibility = View.VISIBLE
        }
    }
}

