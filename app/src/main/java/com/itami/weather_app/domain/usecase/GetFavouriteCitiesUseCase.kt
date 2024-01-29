package com.itami.weather_app.domain.usecase

import com.itami.weather_app.domain.model.City
import com.itami.weather_app.domain.repository.FavouriteRepository
import kotlinx.coroutines.flow.Flow

class GetFavouriteCitiesUseCase(
    private val favouriteRepository: FavouriteRepository
) {

    operator fun invoke(): Flow<List<City>> {
        return favouriteRepository.favouriteCities
    }

}