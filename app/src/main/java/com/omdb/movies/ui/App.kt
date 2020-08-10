package com.omdb.movies.ui

import android.app.Application
import com.omdb.movies.dagger.AppComponent
import com.omdb.movies.dagger.DaggerAppComponent
import com.omdb.movies.dagger.NetworkModule
import com.omdb.movies.dagger.SchedulersModule
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {
    companion object{

        private var appComponent:AppComponent? = null

        fun getInjector(): AppComponent {
            if(appComponent == null){
                appComponent = DaggerAppComponent
                    .builder()
                    .networkModule(NetworkModule)
                    .rxSchedulersModule(SchedulersModule)
                    .build()
            }
            return appComponent as AppComponent
        }

    }

    override fun onCreate() {
        super.onCreate()

    }
}