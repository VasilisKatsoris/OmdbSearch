package com.omdb.movies.ui.moviedetails

import android.os.Bundle
import android.transition.Transition
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.omdb.movies.R
import com.omdb.movies.databinding.FragmentMovieDetailsBinding
import com.omdb.movies.ui.moviesearch.Movie
import com.omdb.movies.ui.base.BaseFragment
import com.omdb.movies.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_movie_details.*

@AndroidEntryPoint
class MovieDetailsFragment: BaseFragment() {

    val viewModel: MovieDetailsViewModel by viewModels()
    lateinit var binding: FragmentMovieDetailsBinding
    var movieRowDao: Movie? = null
    var setViewModelOnEnterAnimationEnd: Boolean = false
    var sharedElementTransitionDone: Boolean = false
    val args: MovieDetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context) .inflateTransition(R.transition.image_shared_element_transition)
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?  ): View? {
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_movie_details, container, false)
        movieRowDao = args.movie
        movieRowDao?.let { movieRowDao ->
            Glide.with(binding.movieDetailsImg).load(movieRowDao.poster)
            .apply(RequestOptions().placeholder(R.drawable.placeholder_poster).error(R.drawable.placeholder_poster).dontAnimate().dontTransform())
            .into(binding.movieDetailsImg)
        }
        binding.movieTitle.text = movieRowDao?.title

        //hide views until it is time to animate them
        viewsToAnimate().forEach {it.visibility = View.INVISIBLE }

        //if api call returns before the transition is complete wait until it finishes to make the transition smoother
        (sharedElementEnterTransition as Transition)?.addListener(object : Utils.MyTransitionListener() {
            override fun onTransitionEnd(p0: Transition?) {
                sharedElementTransitionDone = true
                if(setViewModelOnEnterAnimationEnd){
                    setMovieDetails(viewModel.movieDetails.value)
                }
            }
        })

        //delay entry transition until layout is ready to avoid a possible glitch
        if (savedInstanceState == null) {
            postponeEnterTransition()
        }

        Utils.doOnceOnGlobalLayoutOfView(binding.root, Runnable {startPostponedEnterTransition()})
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieDetailsImg.transitionName = args.transitionName
        movieRowDao?.imdbID?.let { viewModel.getMovieDetails(it) }

        viewModel.movieDetails.observe(viewLifecycleOwner, Observer {
                movieDetailsDao -> setMovieDetails(movieDetailsDao)
        })

        viewModel.isApiCallActive.observe(viewLifecycleOwner, Observer { isApiCallActive ->
            movieDetailsProgressBar.alpha = 1f
            if(!isApiCallActive) {
                movieDetailsProgressBar.animate().alpha(0f).setDuration(250).start()
            }
        })
    }

    private fun setMovieDetails(movieDetails: MovieDetails?) {
        if(sharedElementTransitionDone) {
            binding.viewModel = movieDetails
            animateViewsEntry(viewsToAnimate())
        }
        else{
            setViewModelOnEnterAnimationEnd = true
        }
    }

    private fun viewsToAnimate(): List<View> {
        return listOf(binding.movieTitle, binding.imdbRatingTv,
            binding.metacriticRatingTv, binding.votesTv, binding.moviePlot,
            binding.genreTv, binding.movieYearRatedRuntimeTv, binding.crewMembersTv
        )
    }

}