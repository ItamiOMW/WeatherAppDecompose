package com.itami.weather_app.data.repository

import com.itami.weather_app.domain.model.City
import com.itami.weather_app.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(

): SearchRepository {

    override suspend fun searchCity(query: String): List<City> {
        TODO("Not yet implemented")
    }

}