package com.omdb.movies.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.omdb.movies.ui.base.BaseViewModel
import com.omdb.movies.utils.FileReader
import com.omdb.movies.utils.Response
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.mockito.MockitoAnnotations
import java.net.HttpURLConnection

open abstract class BaseViewModelTest<T:BaseViewModel> {


    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    lateinit var mockServer: MockWebServer

    lateinit var viewModel: T

    @Before
    fun setUpViewModel(){
        MockitoAnnotations.initMocks(this)


        mockServer = MockWebServer()
        mockServer.shutdown()
        mockServer.start(8080)
        viewModel = createViewModel()
    }

    abstract fun createViewModel():T

    fun setNextApiResponse(path:String){
        var mockResponse = MockResponse()
        mockResponse.setResponseCode(if(path == Response.FORCE_API_ERROR) HttpURLConnection.HTTP_INTERNAL_ERROR else HttpURLConnection.HTTP_OK)
        if(path != Response.FORCE_API_ERROR){
            mockResponse.setBody(
                FileReader.readFileAsString(
                    path
                )
            )
        }
        mockServer.enqueue(mockResponse)
    }


    @After
    fun shutDownServer(){
        mockServer.shutdown()
    }

}