package com.omdb.movies.ui.moviesearch

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(var imdbID: String?, var poster: String?, var title: String?, var year: String?) : Parcelable