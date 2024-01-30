package com.itami.weather_app.data.mapper

import com.itami.weather_app.data.local.model.FavouriteCityDbModel
import com.itami.weather_app.domain.model.City

fun FavouriteCityDbModel.toCity() = City(
    id = this.id,
    name = this.name,
    country = this.country
)

fun List<FavouriteCityDbModel>.toCityList() = this.map { it.toCity() }

fun City.toFavouriteCityDbModel() = FavouriteCityDbModel(
    id = this.id,
    name = this.name,
    country = this.country
)