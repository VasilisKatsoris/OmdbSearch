package com.omdb.movies.utils

class Constants {
    companion object {

        //repository values
        const val API_KEY:String = "cbbb32c"
        private const val BASE_URL:String = "http://www.omdbapi.com/"

    }

    fun getBaseUrl(): String {
        return BASE_URL
    }
}