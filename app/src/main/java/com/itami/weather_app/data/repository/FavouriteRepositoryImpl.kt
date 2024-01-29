package com.itami.weather_app.data.repository

import com.itami.weather_app.domain.model.City
import com.itami.weather_app.domain.repository.FavouriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavouriteRepositoryImpl @Inject constructor(

): FavouriteRepository {

    override val favouriteCities: Flow<List<City>>
        get() = TODO("Not yet implemented")

    override fun observeIsFavourite(cityId: Int): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun addToFavourite(city: City) {
        TODO("Not yet implemented")
    }

    override suspend fun removeFromFavourite(cityId: Int) {
        TODO("Not yet implemented")
    }

}