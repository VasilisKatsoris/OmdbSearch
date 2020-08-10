package com.omdb.movies.ui.base

import android.view.View
import androidx.fragment.app.Fragment
import com.omdb.movies.utils.Utils

open class BaseFragment: Fragment() {

    fun animateViewsEntry(views:List<View>){
        var delay=0;

        views.forEach {
            it.visibility = View.VISIBLE
            it.startAnimation(
                Utils.getEntryAnimation(
                    delay
                )
            )
            delay+=50
        }
    }

}