package com.omdb.movies.ui.moviedetails

data class MovieDetails(
    var posterSub:String?, var writer:String?,
    var director:String?, var actors:String?,
    var imdbRating:String?, var imdbVotes:String?,
    var plot:String?, var poster:String?,
    var title:String?, var genre:String?, var metascore:String?) {
}