package com.omdb.movies.utils

import android.view.View

class SafeClickListener(private val clickListener: View.OnClickListener?, private val intervalMs: Long = 1000 ) : View.OnClickListener {

    var lastTimeClicked:Long = 0

    override fun onClick(v: View?) {
        val now = System.currentTimeMillis()
        if(now - lastTimeClicked > intervalMs){
            clickListener?.onClick(v)
            lastTimeClicked = now
        }
    }
}