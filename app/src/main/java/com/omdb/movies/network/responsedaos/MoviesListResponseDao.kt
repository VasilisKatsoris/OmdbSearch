package com.omdb.movies.network.responsedaos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


data class MoviesListResponseDao (
    @SerializedName("Response") val success: Boolean?,
    @SerializedName("Search") val movieResponseDaos: ArrayList<MovieResponseDao>?,
    @SerializedName("totalResults") val totalResults: Int?
)

@Parcelize
data class MovieResponseDao(
    @SerializedName("imdbID") val imdbID: String,
    @SerializedName("Poster") val poster: String,
    @SerializedName("Title") val title: String,
    @SerializedName("Year") val year: String,
    @SerializedName("Type") val type: String
) : Parcelable