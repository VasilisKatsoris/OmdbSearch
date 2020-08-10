package com.omdb.movies.network.transformers

import com.omdb.movies.ui.moviesearch.Movie
import com.omdb.movies.network.responsedaos.MoviesListResponseDao

class MovieListResponseDaoTransformer: Transformer<MoviesListResponseDao, List<Movie>>() {

    override fun transform(responseDao: MoviesListResponseDao):List<Movie>{
        return responseDao.movieResponseDaos?.map { movie -> Movie(movie.imdbID, movie.poster, movie.title, movie.year) }?:ArrayList()
    }

}