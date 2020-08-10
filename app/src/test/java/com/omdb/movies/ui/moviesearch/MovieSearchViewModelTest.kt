package com.omdb.movies.ui.moviesearch

import com.omdb.movies.ui.BaseViewModelTest
import com.omdb.movies.utils.Response
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class MovieSearchViewModelTest: BaseViewModelTest<MovieSearchViewModel>() {

    override fun createViewModel(): MovieSearchViewModel {
        return MovieSearchViewModel()
    }

    @Test
    fun searchForSpider() {
        setNextApiResponse(Response.MOVIES_LIST_SUCCESS_RESPONSE)
        this.viewModel.searchForTitle("spider")
        assertNotNull(this.viewModel.moviesList.value)
        assertNotNull(this.viewModel.totalResults == 10)
    }

    @Test
    fun testPagination() {
        setNextApiResponse(Response.MOVIES_LIST_SUCCESS_RESPONSE)
        viewModel.searchForTitle("spider")
        assert(viewModel.page==1 && viewModel.moviesList.value?.size == 10)

        setNextApiResponse(Response.MOVIES_LIST_SUCCESS_RESPONSE)
        viewModel.loadMore()
        assert(viewModel.page==2 && viewModel.moviesList.value?.size == 20)
    }

    @Test
    fun testExistingResultsKeepShowingAfterErrorOnPaginationCall() {
        setNextApiResponse(Response.MOVIES_LIST_SUCCESS_RESPONSE)
        viewModel.searchForTitle("spider")
        assert(viewModel.page==1 && viewModel.moviesList.value?.size == 10)

        //force view model error handling to occur
        setNextApiResponse(Response.FORCE_API_ERROR)
        viewModel.loadMore()
        assert(viewModel.page==1 && viewModel.moviesList.value?.size == 10)

        //assert that the previously fetched results were not cleared
        setNextApiResponse(Response.MOVIES_LIST_SUCCESS_RESPONSE)
        viewModel.loadMore()
        assert(viewModel.page==2 && viewModel.moviesList.value?.size == 20)
    }

    @Test
    fun testSkipCallWhenAllPaginationResultAreHere() {
        setNextApiResponse(Response.MOVIES_LIST_SUCCESS_RESPONSE)
        this.viewModel.searchForTitle("something")
        setNextApiResponse(Response.MOVIES_LIST_SUCCESS_RESPONSE)
        this.viewModel.loadMore()
        setNextApiResponse(Response.MOVIES_LIST_SUCCESS_RESPONSE)
        this.viewModel.loadMore()
        //total results have been fetched at this point

        setNextApiResponse(Response.MOVIES_LIST_SUCCESS_RESPONSE)
        assert(!this.viewModel.loadMore()) //should skip call
        assert(!this.viewModel.searchForTitle("something")) //should skip call
        assert(this.viewModel.searchForTitle("something else")) //should perform call
    }

    @Test
    fun searchForEmpty() {
        setNextApiResponse(Response.EMPTY_RESPONSE)
        this.viewModel.searchForTitle("")
        assertNull(this.viewModel.moviesList.value)
    }



}