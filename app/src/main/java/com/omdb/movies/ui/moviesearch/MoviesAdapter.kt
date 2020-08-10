package com.omdb.movies.ui.moviesearch

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.ViewPropertyTransition
import com.omdb.movies.R
import com.omdb.movies.ui.base.clickable_adapter.ClickableRecyclerViewAdapter
import com.omdb.movies.ui.base.clickable_adapter.ClickableViewHolder
import com.omdb.movies.ui.base.clickable_adapter.RecyclerViewClickListener


class MoviesAdapter(items: List<Movie>?, onItemClickListener: RecyclerViewClickListener<Movie>)
    : ClickableRecyclerViewAdapter<MoviesAdapter.MovieViewHolder, Movie>(items, onItemClickListener) {

    override fun onViewHolderCreate(viewGroup: ViewGroup?, viewType: Int): MovieViewHolder {
        return MovieViewHolder(inflate(viewGroup, R.layout.movie_view), this)
    }

    override fun onViewHolderBind(holder: MovieViewHolder, data: Movie?, position: Int) {
        holder.setData(data)
    }

    class MovieViewHolder(itemView: View, adapter: MoviesAdapter ) : ClickableViewHolder<Movie>(itemView, adapter) {
        var titleTv: TextView = itemView.findViewById(R.id.movie_title_tv)
        var detailsTv: TextView = itemView.findViewById(R.id.movie_details_tv)
        var posterImg: ImageView = itemView.findViewById(R.id.movie_view_img)

        //use this to achieve crossfading instead of Glide default crossfade
        //to avoid a Glide bug that skews the image at first load
        private val animationObject = ViewPropertyTransition.Animator {
            it.alpha = 0f
            it.animate().alpha(1f).setDuration(450).start()
        }

        fun setData(movie: Movie?) {

            if(movie!=null) {
                titleTv.text = movie.title
                detailsTv.text = movie.year
                posterImg.setImageResource(R.drawable.placeholder_poster)
                posterImg.transitionName = movie.poster + movie.title //set unique transition name

                Glide.with(posterImg)
                    .load(movie.poster)
                    .transition(GenericTransitionOptions.with(animationObject))
                    .apply(RequestOptions()
                        .placeholder(R.drawable.placeholder_poster)
                        .error(R.drawable.placeholder_poster)
                        .dontTransform())
                    .into(posterImg)
            }
        }

    }

}