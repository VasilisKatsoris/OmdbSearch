package com.omdb.movies.ui.moviedetails

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.omdb.movies.network.responsedaos.MovieDetailsResponseDao
import com.omdb.movies.ui.base.BaseViewModel
import com.omdb.movies.network.transformers.MovieDetailsResponseDaoTransformer
import io.reactivex.disposables.Disposable

class MovieDetailsViewModel @ViewModelInject constructor(): BaseViewModel(){

    val movieDetails:MutableLiveData<MovieDetails> = MutableLiveData()
    private val transformer = MovieDetailsResponseDaoTransformer()
    private var subscription: Disposable? = null

    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }

    fun getMovieDetails(imdbId:String){
        subscription?.dispose()
        subscription = restRepository.getMovieDetailsForImdbId(imdbId)
            .subscribeOn(ioScheduler)
            .observeOn(mainThreadScheduler)
            .doOnSubscribe { setApiCallActive(true) }
            .doOnTerminate { setApiCallActive(false) }
            .subscribe(
                { result -> onRetrievePostListSuccess(result) },
                { onRetrieveMovieListDaoError() }
            )
    }

    private fun onRetrievePostListSuccess(movieDetailsResponseResponseDao: MovieDetailsResponseDao){
        movieDetails.value = transformer.transform(movieDetailsResponseResponseDao)
    }

    private fun onRetrieveMovieListDaoError(){
        setApiCallActive(false)
        movieDetails.value = null
    }
}
