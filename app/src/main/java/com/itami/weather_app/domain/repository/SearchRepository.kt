package com.itami.weather_app.domain.repository

import com.itami.weather_app.domain.model.City

interface SearchRepository {

    suspend fun searchCity(query: String): List<City>

}