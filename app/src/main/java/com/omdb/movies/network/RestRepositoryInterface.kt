package com.omdb.movies.network

import com.omdb.movies.network.responsedaos.MovieDetailsResponseDao
import com.omdb.movies.network.responsedaos.MoviesListResponseDao
import io.reactivex.Observable

interface RestRepositoryInterface{
    fun searchMoviesFor(s: String, page: Int): Observable<MoviesListResponseDao>
    fun getMovieDetailsForImdbId(imdbId: String): Observable<MovieDetailsResponseDao>
}