package com.itami.weather_app.domain.repository

import com.itami.weather_app.domain.model.City
import com.itami.weather_app.domain.util.AppResponse

interface SearchRepository {

    suspend fun searchCity(query: String): AppResponse<List<City>>

}