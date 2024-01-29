package com.itami.weather_app.domain.usecase

import com.itami.weather_app.domain.repository.FavouriteRepository
import kotlinx.coroutines.flow.Flow

class ObserveIsFavouriteUseCase(
    private val favouriteRepository: FavouriteRepository
) {

    operator fun invoke(cityId: Int): Flow<Boolean> {
        return favouriteRepository.observeIsFavourite(cityId = cityId)
    }

}