package com.itami.weather_app.data.repository

import com.itami.weather_app.data.local.dao.FavouriteCitiesDao
import com.itami.weather_app.data.mapper.toCityList
import com.itami.weather_app.data.mapper.toFavouriteCityDbModel
import com.itami.weather_app.domain.model.City
import com.itami.weather_app.domain.repository.FavouriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavouriteRepositoryImpl @Inject constructor(
    private val favouriteCitiesDao: FavouriteCitiesDao,
) : FavouriteRepository {

    override val favouriteCities: Flow<List<City>> =
        favouriteCitiesDao.getFavouriteCities().map { list -> list.toCityList() }

    override fun observeIsFavourite(cityId: Int): Flow<Boolean> {
        return favouriteCitiesDao.observeIsFavourite(cityId = cityId)
    }

    override suspend fun addToFavourite(city: City) {
        val cityDbModel = city.toFavouriteCityDbModel()
        favouriteCitiesDao.addToFavourite(cityDbModel = cityDbModel)
    }

    override suspend fun removeFromFavourite(cityId: Int) {
        favouriteCitiesDao.removeFromFavourite(cityId = cityId)
    }

}