package com.itami.weather_app.presentation.search

import com.arkivanov.decompose.ComponentContext
import javax.inject.Inject

class DefaultSearchComponent @Inject constructor(
    private val componentContext: ComponentContext
) : SearchComponent, ComponentContext by componentContext {

    

}