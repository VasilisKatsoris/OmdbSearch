package com.omdb.movies.ui.base.clickable_adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.omdb.movies.utils.setSafeClickListener

open class ClickableViewHolder<Data>(
    v: View,
    adapter: ClickableRecyclerViewAdapter<*,Data>
) : RecyclerView.ViewHolder(v) {

    var position:Int? = 0
    var data: Data? = null

    init {
        v.setSafeClickListener(View.OnClickListener {
            adapter.recyclerViewClickListener?.onItemClicked(data, this@ClickableViewHolder, position)
        })
    }
}