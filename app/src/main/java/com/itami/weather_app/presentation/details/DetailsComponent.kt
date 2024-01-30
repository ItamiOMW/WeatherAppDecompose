package com.itami.weather_app.presentation.details

import kotlinx.coroutines.flow.StateFlow

interface DetailsComponent {

    val state: StateFlow<DetailsStore.State>

    fun onNavigateBackClick()

    fun onChangeFavouriteClick()

}