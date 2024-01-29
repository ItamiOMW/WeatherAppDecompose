package com.itami.weather_app.data.mapper

import com.itami.weather_app.data.remote.dto.WeatherCurrentDto
import com.itami.weather_app.data.remote.dto.WeatherDto
import com.itami.weather_app.data.remote.dto.WeatherForecastDto
import com.itami.weather_app.domain.model.Forecast
import com.itami.weather_app.domain.model.Weather
import com.itami.weather_app.utils.secondsToCalendar

fun WeatherDto.toWeather() = Weather(
    tempC = this.tempC,
    condition = this.conditionDto.text,
    conditionIconUrl = correctConditionIconUrl(this.conditionDto.iconUrl),
    date = this.date.secondsToCalendar()
)

fun WeatherCurrentDto.toWeather() = this.weatherDto.toWeather()

fun WeatherForecastDto.toForecast() = Forecast(
    currentWeather = this.weatherDto.toWeather(),
    upcomingWeather = this.forecastDto.forecastDayDtos.drop(1).map { dayDto ->
        val dayWeatherDto = dayDto.dayWeatherDto
        Weather(
            tempC = dayWeatherDto.tempC,
            condition = dayWeatherDto.conditionDto.text,
            conditionIconUrl = correctConditionIconUrl(dayWeatherDto.conditionDto.iconUrl),
            date = dayDto.date.secondsToCalendar()
        )
    }
)

private fun correctConditionIconUrl(iconUrl: String): String {
    return "https:$iconUrl".replace(oldValue = "64x64", newValue = "128x128")
}