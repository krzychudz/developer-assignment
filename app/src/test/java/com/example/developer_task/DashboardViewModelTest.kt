package com.example.developer_task

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.developer_task.backend.ApiClient
import com.example.developer_task.enums.DashboardScreenState
import com.example.developer_task.models.ComicsData
import com.example.developer_task.models.ComicsModel
import com.example.developer_task.models.ComicsResponse
import com.example.developer_task.models.ThumbnailModel
import com.example.developer_task.viewmodels.dashboard.DashboardViewModel
import com.example.developer_task.viewmodels.states.ComicsListState
import io.reactivex.Single
import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations


class DashboardViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    @Mock
    lateinit var apiClient: ApiClient
    lateinit var dashboardViewModel: DashboardViewModel
    @Mock
    lateinit var stateObserver: Observer<ComicsListState>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        dashboardViewModel = DashboardViewModel(apiClient)
        dashboardViewModel.comicsData.observeForever(stateObserver)
    }

    @Test
    fun testNull() {
        `when`<Any?>(apiClient.getComicsData("0","test")).thenReturn(null)
        assertNotNull(dashboardViewModel.comicsData);
        assertTrue(dashboardViewModel.comicsData.hasObservers());
    }

    @Test
    fun testApiFetchDataEmpty() {
        `when`<Any>(apiClient.getComicsData("0", "")).thenReturn(Single.just(ComicsResponse(
            ComicsData(
                0,
                25,
                25,
                25,
                emptyList()
            )
        )))

        dashboardViewModel.getComicsList("0", "")
        verify(stateObserver).onChanged(ComicsListState.ERROR_STATE("Sorry, no results found"))
    }

    @Test
    fun testApiFetchDataSuccess() {
        `when`<Any>(apiClient.getComicsData("0", "")).thenReturn(Single.just(ComicsResponse(
            ComicsData(
                0,
                25,
                25,
                25,
                listOf(ComicsModel("testTitle", "testDescription",
                    ThumbnailModel("testPath", "testExt")))
            )
        )))
        dashboardViewModel.getComicsList("0", "")
        assertEquals(dashboardViewModel.comicsData.value!!.state, DashboardScreenState.COMICS_FETCH_SUCCESS_STATE)
    }
}