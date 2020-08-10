package com.omdb.movies.ui.base.clickable_adapter

interface RecyclerViewClickListener<DataClass> {
    fun onItemClicked(
        data: DataClass?,
        holder: ClickableViewHolder<DataClass>,
        position: Int?
    )
}