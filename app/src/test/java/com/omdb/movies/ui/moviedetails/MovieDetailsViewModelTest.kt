package com.omdb.movies.ui.moviedetails

import com.omdb.movies.ui.BaseViewModelTest
import com.omdb.movies.utils.Response
import junit.framework.Assert.assertNull
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class MovieDetailsViewModelTest: BaseViewModelTest<MovieDetailsViewModel>() {

    override fun createViewModel(): MovieDetailsViewModel {
        return MovieDetailsViewModel()
    }

    @Test
    fun getMovieDetailsSuccess() {
        setNextApiResponse(Response.MOVIE_DETAILS_SUCCESS_RESPONSE)
        this.viewModel.getMovieDetails("tt0145487")
        assertNotNull(this.viewModel.movieDetails.value)
    }

    @Test
    fun getMovieDetailsError() {
        setNextApiResponse(Response.FORCE_API_ERROR)
        this.viewModel.getMovieDetails("tt0145487")
        assertNull(this.viewModel.movieDetails.value)
    }

}