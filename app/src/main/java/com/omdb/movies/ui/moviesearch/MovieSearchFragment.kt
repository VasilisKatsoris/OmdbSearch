package com.omdb.movies.ui.moviesearch

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.transition.TransitionSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.omdb.movies.R
import com.omdb.movies.ui.base.BaseFragment
import com.omdb.movies.ui.base.clickable_adapter.ClickableViewHolder
import com.omdb.movies.ui.base.clickable_adapter.RecyclerViewClickListener
import com.omdb.movies.utils.PagingScrollListener
import com.omdb.movies.utils.Utils
import com.omdb.movies.utils.expandSearchView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_movie_details.*
import kotlinx.android.synthetic.main.fragment_movies_list.*


@AndroidEntryPoint
class MovieSearchFragment: BaseFragment(), RecyclerViewClickListener<Movie> {

    val viewModel: MovieSearchViewModel by viewModels()
    private var adapter: MoviesAdapter? = null
    private val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
          confirmExit()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = Utils.getFragmentExitTransition()
        activity?.onBackPressedDispatcher?.addCallback(this, callback)
    }



    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?  ): View? {
        return inflater.inflate(R.layout.fragment_movies_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        setUpSearchEditText()

        viewModel.moviesList.observe(viewLifecycleOwner, Observer {
                moviesList -> showData(moviesList)
        })
        viewModel.isApiCallActive.observe(viewLifecycleOwner, Observer {
                isApiCallActive -> search_progress_bar.visibility = if(isApiCallActive) VISIBLE else GONE
        })

        Utils.doOnceOnGlobalLayoutOfView(recycler_view, Runnable {
            if(titleTv.visibility == VISIBLE) {
                animateViewsEntry(listOf(titleTv, searchview, messageTv))
            }
        })

        if(exitTransition!=null) {
            (exitTransition as TransitionSet).excludeTarget(movieDetailsImg, true)
        }
    }

    private fun setUpSearchEditText(){
        val handler = Handler(Looper.getMainLooper())
        val runnable = Runnable {
            if(!hasConnection()){
                showConnectionDialog()
                return@Runnable
            }
            val searchText: String = searchview.query.toString()
            if (searchText.trim().isNotEmpty()) {
                viewModel.searchForTitle(searchText)
            } else {
                showData(ArrayList())
            }
        }

        searchview.setOnQueryTextListener(object :Utils.SearchQueryListener(){
            val TYPE_SEARCH_TIMEOUT = 500L
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.isEmpty()?.let { titleTv.visibility = if (it) VISIBLE else GONE }
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, TYPE_SEARCH_TIMEOUT)
                return false
            }
        })

        titleTv.setOnClickListener { searchview.expandSearchView() }
    }


    private fun setUpRecyclerView(){
        val layoutManager = LinearLayoutManager(context)
        recycler_view.layoutManager = layoutManager
        recycler_view.addItemDecoration(DividerItemDecoration( context, RecyclerView.VERTICAL))

        if(adapter == null) {
            adapter = MoviesAdapter(ArrayList(), this)
        }
        recycler_view.adapter = adapter
        showData(viewModel.moviesList.value)
        postponeEnterTransition()

        recycler_view.addOnScrollListener(object : PagingScrollListener(layoutManager){
            override fun loadMore() {
                val loading:Boolean = viewModel.isApiCallActive.value?:false
                val searchQueryMatchesFetchedResults = searchview.query.toString() == viewModel.searchText
                if(!loading && searchQueryMatchesFetchedResults && hasConnection() ){
                    viewModel.loadMore()
                }
            }
        })

    }

    private fun showData(movies: List<Movie>?) {
        val moviesList = movies?: ArrayList()
        adapter?.let {  adapter->
            adapter.setData(moviesList)
            adapter.notifyDataSetChanged()
        }
        messageTv.visibility = if(moviesList.isEmpty()) VISIBLE else GONE

        messageTv.setText(if(searchview.query.isEmpty()) R.string.welcome_to_movie_app else R.string.No_results_found)
        Utils.doOnceOnGlobalLayoutOfView(view, Runnable {startPostponedEnterTransition()})
    }

    override fun onItemClicked(data: Movie?, holder: ClickableViewHolder<Movie>, position: Int?) {
        Utils.hideKeyboard(searchview)
        if(!hasConnection()){
            showConnectionDialog()
            return
        }

        val posterImg = (holder as MoviesAdapter.MovieViewHolder).posterImg
        val action = data?.let { MovieSearchFragmentDirections.actionSearchFragmentToDetailsFragment(it, posterImg.transitionName) }
        val extras = FragmentNavigatorExtras(
            posterImg to posterImg.transitionName
        )
        action?.let { findNavController().navigate(it, extras) }
    }

    private fun hasConnection(): Boolean {
        return context?.let { Utils.isConnected(it) }?:true
    }
    private fun showConnectionDialog(){
        context?.let {
            AlertDialog.Builder(it)
                .setMessage(getString(R.string.No_connection))
                .setNegativeButton(getString(R.string.Ok)) { dialog, i -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    fun confirmExit() {
        context?.let {
            AlertDialog.Builder(it)
                .setMessage(getString(R.string.Exit_questionmark))
                .setPositiveButton(getString(R.string.Ok)) { dialog, i -> activity?.finish() }
                .setNegativeButton(getString(R.string.Cancel)) { dialog, i -> dialog.dismiss() }
                .create()
                .show()
        }
    }

}


