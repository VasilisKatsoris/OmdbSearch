package com.omdb.movies.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.transition.Fade
import android.transition.Slide
import android.transition.Transition
import android.transition.TransitionSet
import android.view.Gravity
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.animation.*
import android.view.inputmethod.InputMethodManager


class Utils {

    open class AnimListener:Animation.AnimationListener{
        override fun onAnimationRepeat(p0: Animation?) {
        }

        override fun onAnimationEnd(p0: Animation?) {
        }

        override fun onAnimationStart(p0: Animation?) {
        }

    }


    open class MyTransitionListener: Transition.TransitionListener {
        override fun onTransitionEnd(p0: Transition?) {
        }

        override fun onTransitionResume(p0: Transition?) {
        }

        override fun onTransitionPause(p0: Transition?) {
        }

        override fun onTransitionCancel(p0: Transition?) {
        }

        override fun onTransitionStart(p0: Transition?) {
        }

    }

    open class SearchQueryListener:androidx.appcompat.widget.SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return false;
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return false;
        }

    }




    companion object {
        fun hideKeyboard(view: View?) {
            if (view == null || view.context == null) {
                return
            }
            val imm = view.context
                .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun getRightToLeftAnimation(relativeTo: Int): Animation? {
            return TranslateAnimation(relativeTo, 1f, relativeTo, 0f, relativeTo, 0f, relativeTo, 0f)
        }

        fun getEntryAnimation(delay: Int): AnimationSet? {
            val set = AnimationSet(true)
            val translateAnim: Animation? = Utils.getRightToLeftAnimation(Animation.RELATIVE_TO_PARENT)
            set.addAnimation(AlphaAnimation(-8f, 1f))
            set.addAnimation(translateAnim)
            set.startOffset = delay.toLong()
            set.duration = 400
            set.interpolator = DecelerateInterpolator(2.5f)
            return set
        }


        fun doOnceOnGlobalLayoutOfView(  v: View?,  r: Runnable?  ) {
            v?.viewTreeObserver?.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    v.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                    r?.run()
                }
            })
        }


        fun getFragmentExitTransition(): TransitionSet? {

            val slide = Slide()
            slide.slideEdge = Gravity.LEFT

            return TransitionSet()
                .addTransition(slide)
                .addTransition(Fade())
                .setDuration(250)
                .setInterpolator(DecelerateInterpolator(2f))
        }


        fun isConnected(context: Context): Boolean {
            var result = 0 // connection type. 0: none; 1: mobile data; 2: wifi
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
                if (capabilities != null) {
                    result = when {
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> { 2 }
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> { 1 }
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> { 3 }
                        else -> 0
                    }
                }
            } else {
                val activeNetwork = cm.activeNetworkInfo
                if (activeNetwork != null) {
                    // connected to the internet
                    result = when (activeNetwork.type) {
                        ConnectivityManager.TYPE_WIFI -> { 2 }
                        ConnectivityManager.TYPE_MOBILE -> { 1 }
                        ConnectivityManager.TYPE_VPN -> { 3 }
                        else -> 0
                    }
                }
            }
            return result!=0
        }
    }
}