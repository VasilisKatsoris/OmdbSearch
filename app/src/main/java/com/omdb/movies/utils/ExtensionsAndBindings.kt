package com.omdb.movies.utils

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

fun androidx.appcompat.widget.SearchView.expandSearchView() {
    setIconifiedByDefault(true)
    isFocusable = true
    isIconified = false
    requestFocusFromTouch()
}

@BindingAdapter("onSingleClick")
fun View.setSafeClickListener(clickListener: View.OnClickListener?) {
    setOnClickListener(SafeClickListener(clickListener))
}

@BindingAdapter("glideImageUrl")
fun setGlideImageUrl(imageView: ImageView, text: String?) {
    Glide.with(imageView).load(text).transition(DrawableTransitionOptions.withCrossFade()).into(imageView)
}
