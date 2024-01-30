package com.itami.weather_app.data.mapper

import com.itami.weather_app.data.remote.dto.CityDto
import com.itami.weather_app.domain.model.City


fun CityDto.toCity() = City(
    id = this.id,
    name = this.name,
    country = this.country
)

fun List<CityDto>.toCityList() = this.map { it.toCity() }