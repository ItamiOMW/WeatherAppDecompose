package com.itami.weather_app.presentation.search

import com.itami.weather_app.domain.model.City
import kotlinx.coroutines.flow.StateFlow

interface SearchComponent {

    val state: StateFlow<SearchStore.State>

    fun onCityClick(city: City)

    fun onNavigateBackClick()

}