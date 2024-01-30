package com.itami.weather_app.domain.usecase

import com.itami.weather_app.domain.model.City
import com.itami.weather_app.domain.repository.FavouriteRepository

class ChangeFavouriteUseCase(
    private val favouriteRepository: FavouriteRepository
) {

    suspend operator fun invoke(city: City, isFavouriteNow: Boolean) {
        if (isFavouriteNow) {
            removeFromFavourite(cityId = city.id)
        } else {
            addToFavourite(city = city)
        }
    }

    suspend fun addToFavourite(city: City) {
        favouriteRepository.addToFavourite(city = city)
    }

    suspend fun removeFromFavourite(cityId: Int) {
        favouriteRepository.removeFromFavourite(cityId = cityId)
    }

}