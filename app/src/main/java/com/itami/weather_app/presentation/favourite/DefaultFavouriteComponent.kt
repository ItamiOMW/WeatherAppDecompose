package com.itami.weather_app.presentation.favourite

import com.arkivanov.decompose.ComponentContext
import javax.inject.Inject

class DefaultFavouriteComponent @Inject constructor(
    private val componentContext: ComponentContext
) : FavouriteComponent, ComponentContext by componentContext {

    

}