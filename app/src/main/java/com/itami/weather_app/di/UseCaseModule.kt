package com.itami.weather_app.di

import com.itami.weather_app.domain.repository.FavouriteRepository
import com.itami.weather_app.domain.repository.SearchRepository
import com.itami.weather_app.domain.repository.WeatherRepository
import com.itami.weather_app.domain.usecase.ChangeFavouriteUseCase
import com.itami.weather_app.domain.usecase.GetFavouriteCitiesUseCase
import com.itami.weather_app.domain.usecase.GetForecastUseCase
import com.itami.weather_app.domain.usecase.GetWeatherUseCase
import com.itami.weather_app.domain.usecase.ObserveIsFavouriteUseCase
import com.itami.weather_app.domain.usecase.SearchCityUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideChangeFavouriteUseCase(
        favouriteRepository: FavouriteRepository
    ): ChangeFavouriteUseCase = ChangeFavouriteUseCase(favouriteRepository = favouriteRepository)

    @Provides
    @Singleton
    fun provideObserveIsFavouriteUseCase(
        favouriteRepository: FavouriteRepository
    ): ObserveIsFavouriteUseCase = ObserveIsFavouriteUseCase(favouriteRepository = favouriteRepository)

    @Provides
    @Singleton
    fun provideGetFavouriteCitiesUseCase(
        favouriteRepository: FavouriteRepository
    ): GetFavouriteCitiesUseCase = GetFavouriteCitiesUseCase(favouriteRepository = favouriteRepository)

    @Provides
    @Singleton
    fun provideSearchCityUseCase(
        searchRepository: SearchRepository
    ): SearchCityUseCase = SearchCityUseCase(searchRepository = searchRepository)

    @Provides
    @Singleton
    fun provideGetWeatherUseCase(
        weatherRepository: WeatherRepository
    ): GetWeatherUseCase = GetWeatherUseCase(weatherRepository = weatherRepository)

    @Provides
    @Singleton
    fun provideGetForecastUseCase(
        weatherRepository: WeatherRepository
    ): GetForecastUseCase = GetForecastUseCase(weatherRepository = weatherRepository)

}