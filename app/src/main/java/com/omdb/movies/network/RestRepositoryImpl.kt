package com.omdb.movies.network

import com.omdb.movies.network.responsedaos.MovieDetailsResponseDao
import com.omdb.movies.network.responsedaos.MoviesListResponseDao
import io.reactivex.Observable
import net.gahfy.mvvmMovies.network.ApiCalls
import javax.inject.Inject

class RestRepositoryImpl @Inject constructor (private val api:ApiCalls):
    RestRepositoryInterface {

    override fun searchMoviesFor(s: String, page: Int): Observable<MoviesListResponseDao> {
        return api.searchMoviesFor(s, page)
    }

    override fun getMovieDetailsForImdbId(imdbId: String): Observable<MovieDetailsResponseDao> {
        return api.getMovieDetailsForImdbId(imdbId)
    }
}