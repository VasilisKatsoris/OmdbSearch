package com.omdb.movies.ui.moviesearch

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.omdb.movies.network.responsedaos.MoviesListResponseDao
import com.omdb.movies.ui.base.BaseViewModel
import com.omdb.movies.network.transformers.MovieListResponseDaoTransformer
import io.reactivex.disposables.Disposable

class MovieSearchViewModel @ViewModelInject constructor(): BaseViewModel(){

    var page = 0
    var searchText:String? = null
    val moviesList:MutableLiveData<ArrayList<Movie>> = MutableLiveData()
    var totalResults: Int = 0
    private val transformer = MovieListResponseDaoTransformer()
    private var subscription: Disposable? = null

    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }

    fun searchForTitle(searchQuery: String): Boolean {
        return searchForTitle(searchQuery, false)
    }

    private fun searchForTitle(searchQuery:String, loadNextPage:Boolean):Boolean{
        val searchQueryTrimmed = searchQuery.trim()
        //skip searches with the same text as the last one if a valid search result is already here
        //if the search query is the same as before but the current data is null then we must let the search continue
        if(!loadNextPage && searchText==searchQueryTrimmed && moviesList.value!=null){
            moviesList.value = moviesList.value //notify the view to show the data
            return false
        }

        //no need to load more data because all has been fetched
        if(loadNextPage && moviesList.value?.size == totalResults){
            return false
        }
        //if the search query is new reset the pagination data
        if(searchText!=searchQueryTrimmed){
            page = 0
            totalResults = 0
        }

        this.searchText = searchQueryTrimmed
        subscription?.dispose()
        subscription = restRepository.searchMoviesFor(searchQueryTrimmed, page+1)
            .subscribeOn(ioScheduler)
            .observeOn(mainThreadScheduler)
            .doOnSubscribe { setApiCallActive(true) }
            .doOnTerminate { setApiCallActive(false) }
            .subscribe(
                { result -> onRetrievePostListSuccess(result) },
                { onRetrieveMovieListDaoError() }
            )
        return true
    }

    fun loadMore(): Boolean {
        return searchText?.let { searchForTitle(it, true) }?:false
    }

    private fun onRetrievePostListSuccess(moviesListResponseDao: MoviesListResponseDao){
        if(moviesListResponseDao.success == true) {
            totalResults = moviesListResponseDao.totalResults?:0
            page++
            val movieRows = transformer.transform(moviesListResponseDao)

            if(page<=1 || this.moviesList.value == null){
                this.moviesList.value = ArrayList(movieRows)
            }
            else if(page>1){
                this.moviesList.value?.addAll(movieRows)
            }
        }
        else if(page == 0){
            this.moviesList.value = null
        }
    }

    private fun onRetrieveMovieListDaoError(){
        if(page == 0) {
            this.moviesList.value = null
        }
    }
}
