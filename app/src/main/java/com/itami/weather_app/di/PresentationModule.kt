package com.itami.weather_app.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.itami.weather_app.presentation.favourite.FavouriteStore
import com.itami.weather_app.presentation.favourite.FavouriteStoreFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PresentationModule {

    @Provides
    @Singleton
    fun provideStoreFactory(): StoreFactory = DefaultStoreFactory()

    @Provides
    @Singleton
    fun provideFavouriteStore(
        favouriteStoreFactory: FavouriteStoreFactory
    ): FavouriteStore = favouriteStoreFactory.create()

}