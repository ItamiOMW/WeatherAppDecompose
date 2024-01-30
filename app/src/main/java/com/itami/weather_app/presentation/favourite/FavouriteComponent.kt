package com.itami.weather_app.presentation.favourite

import com.itami.weather_app.domain.model.City
import kotlinx.coroutines.flow.StateFlow

interface FavouriteComponent {

    val state: StateFlow<FavouriteStore.State>

    fun onSearchClick()

    fun onAddFavouriteClick()

    fun onCityItemClick(cityItem: City)

}