package com.omdb.movies.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PagingScrollListener(val layoutManager: LinearLayoutManager): RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val totalItemCount = layoutManager.itemCount
        val visibleItemCount = layoutManager.childCount
        val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        var VISIBLE_THRESHOLD = 4
        if (visibleItemCount + firstVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount &&
            firstVisibleItemPosition >= 0 ) {
            loadMore()
        }

    }

    abstract fun loadMore()

}