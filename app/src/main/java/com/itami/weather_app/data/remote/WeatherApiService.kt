package com.itami.weather_app.data.remote

import com.itami.weather_app.data.remote.dto.CityDto
import com.itami.weather_app.data.remote.dto.WeatherCurrentDto
import com.itami.weather_app.data.remote.dto.WeatherForecastDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("current.json")
    suspend fun loadCurrentWeather(
        @Query("q") query: String
    ): Response<WeatherCurrentDto>

    @GET("forecast.json")
    suspend fun loadForecast(
        @Query("q") query: String,
        @Query("days") days: Int = 4,
    ): Response<WeatherForecastDto>

    @GET("search.json")
    suspend fun searchCity(
        @Query("q") query: String,
    ): Response<List<CityDto>>

    companion object {

        const val BASE_URL = "https://api.weatherapi.com/v1/"

    }

}