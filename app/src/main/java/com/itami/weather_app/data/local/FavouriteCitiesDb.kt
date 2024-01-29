package com.itami.weather_app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.itami.weather_app.data.local.dao.FavouriteCitiesDao
import com.itami.weather_app.data.local.model.FavouriteCityDbModel

@Database(
    entities = [FavouriteCityDbModel::class],
    version = 1,
    exportSchema = false
)
abstract class FavouriteCitiesDb: RoomDatabase() {

    abstract fun favouriteCitiesDao(): FavouriteCitiesDao

    companion object {

        const val DB_NAME = "FavouriteDatabase"

    }
}