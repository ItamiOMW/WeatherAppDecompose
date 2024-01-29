package com.itami.weather_app.di

import android.app.Application
import androidx.room.Room
import com.itami.weather_app.data.local.FavouriteCitiesDb
import com.itami.weather_app.data.local.dao.FavouriteCitiesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideFavouriteCitiesDao(
        db: FavouriteCitiesDb,
    ): FavouriteCitiesDao = db.favouriteCitiesDao()

    @Provides
    @Singleton
    fun provideFavouriteDatabase(
        application: Application,
    ): FavouriteCitiesDb {
        return Room.databaseBuilder(
            context = application,
            klass = FavouriteCitiesDb::class.java,
            name = FavouriteCitiesDb.DB_NAME
        ).build()
    }

}