package com.itami.weather_app.di

import android.app.Application
import com.itami.weather_app.data.local.dao.FavouriteCitiesDao
import com.itami.weather_app.data.remote.WeatherApiService
import com.itami.weather_app.data.repository.FavouriteRepositoryImpl
import com.itami.weather_app.data.repository.SearchRepositoryImpl
import com.itami.weather_app.data.repository.WeatherRepositoryImpl
import com.itami.weather_app.domain.repository.FavouriteRepository
import com.itami.weather_app.domain.repository.SearchRepository
import com.itami.weather_app.domain.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideFavouriteRepository(
        favouriteCitiesDao: FavouriteCitiesDao
    ): FavouriteRepository = FavouriteRepositoryImpl(favouriteCitiesDao)

    @Provides
    @Singleton
    fun provideWeatherRepository(
        weatherApiService: WeatherApiService,
        application: Application
    ): WeatherRepository = WeatherRepositoryImpl(
        weatherApiService = weatherApiService,
        application = application
    )

    @Provides
    @Singleton
    fun provideSearchRepository(
        weatherApiService: WeatherApiService,
        application: Application
    ): SearchRepository = SearchRepositoryImpl(
        weatherApiService = weatherApiService,
        application = application
    )

}