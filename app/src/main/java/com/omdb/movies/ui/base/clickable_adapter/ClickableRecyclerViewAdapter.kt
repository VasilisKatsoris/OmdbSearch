package com.omdb.movies.ui.base.clickable_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

abstract class ClickableRecyclerViewAdapter<ViewHolderClass : ClickableViewHolder<DataClass>, DataClass>(
    items: List<DataClass>?,
    onItemClickListener: RecyclerViewClickListener<DataClass>
) : RecyclerView.Adapter<ViewHolderClass>() {

    var items: List<DataClass>? = null
    var recyclerViewClickListener: RecyclerViewClickListener<DataClass>? = null

    fun getItem(position: Int): DataClass? {
        return if (items != null) items!![position] else null
    }

    fun setData(items: List<DataClass>?) {
        this.items = items
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolderClass {
        return onViewHolderCreate(viewGroup, i)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        holder.position = position
        holder.data =getItem(position)
        onViewHolderBind(holder, getItem(position), position)
    }

    override fun getItemCount(): Int {
        return if (items != null) items!!.size else 0
    }

    abstract fun onViewHolderCreate(viewGroup: ViewGroup?, viewType: Int): ViewHolderClass
    abstract fun onViewHolderBind(
        holder: ViewHolderClass,
        data: DataClass?,
        position: Int
    )

    fun inflate(viewGroup: ViewGroup?, @LayoutRes layoutResId: Int): View {
        return LayoutInflater.from(viewGroup?.context).inflate(layoutResId, viewGroup, false)
    }

    init {
        recyclerViewClickListener = onItemClickListener
        setData(items)
    }
}