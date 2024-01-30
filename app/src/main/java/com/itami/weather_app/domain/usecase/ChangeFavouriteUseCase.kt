package com.itami.weather_app.domain.usecase

import com.itami.weather_app.domain.model.City
import com.itami.weather_app.domain.repository.FavouriteRepository

class ChangeFavouriteUseCase(
    private val favouriteRepository: FavouriteRepository
) {

    suspend operator fun invoke(city: City, isFavouriteNow: Boolean) {
        if (isFavouriteNow) {
            favouriteRepository.removeFromFavourite(cityId = city.id)
        } else {
            favouriteRepository.addToFavourite(city = city)
        }
    }

}