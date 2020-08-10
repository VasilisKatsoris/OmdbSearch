package com.omdb.movies.dagger

import com.omdb.movies.network.RestRepositoryImpl
import com.omdb.movies.network.RestRepositoryInterface
import com.omdb.movies.network.RetrofitBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import io.reactivex.Scheduler
import net.gahfy.mvvmMovies.network.ApiCalls
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    internal fun provideApiCalls(retrofit: Retrofit): ApiCalls {
        return retrofit.create(ApiCalls::class.java)
    }

    @Singleton
    @Provides
    internal fun provideRetrofit(@SchedulersModule.IoScheduler scheduler:Scheduler): Retrofit {
        return RetrofitBuilder(scheduler).retrofit
    }

    @Singleton
    @Provides
    internal fun provideRestRepository(apiCalls: ApiCalls): RestRepositoryInterface {
        return RestRepositoryImpl(apiCalls)
    }

}