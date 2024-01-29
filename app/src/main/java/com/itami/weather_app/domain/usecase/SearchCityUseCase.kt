package com.itami.weather_app.domain.usecase

import com.itami.weather_app.domain.model.City
import com.itami.weather_app.domain.repository.SearchRepository

class SearchCityUseCase(
    private val searchRepository: SearchRepository
) {

    suspend operator fun invoke(query: String): List<City> {
        return searchRepository.searchCity(query = query)
    }

}