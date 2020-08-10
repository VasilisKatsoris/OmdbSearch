package com.omdb.movies.dagger

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import javax.inject.Qualifier


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
    internal fun provideIoScheduler(): Scheduler {
        return Schedulers.trampoline()
    }

    @MainThreadScheduler
    @Provides
    internal fun provideMainThreadScheduler(): Scheduler {
        return Schedulers.trampoline()
    }

}