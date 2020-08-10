package com.omdb.movies.dagger

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object SchedulersModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class IoScheduler

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class MainThreadScheduler



    @IoScheduler
    @Provides
    @Singleton
    internal fun provideIoScheduler(): Scheduler {
        return Schedulers.io()
    }

    @MainThreadScheduler
    @Provides
    @Singleton
    internal fun provideMainThreadScheduler(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

}