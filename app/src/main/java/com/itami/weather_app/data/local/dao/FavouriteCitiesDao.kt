package com.itami.weather_app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.itami.weather_app.data.local.model.FavouriteCityDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteCitiesDao {

    @Query("SELECT * FROM favourite_cities")
    fun getFavouriteCities(): Flow<List<FavouriteCityDbModel>>

    @Query("SELECT EXISTS (SELECT * FROM favourite_cities WHERE id=:cityId LIMIT 1)")
    fun observeIsFavourite(cityId: Int): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavourite(cityDbModel: FavouriteCityDbModel)

    @Query("DELETE FROM favourite_cities WHERE id=:cityId")
    suspend fun removeFromFavourite(cityId: Int)

}