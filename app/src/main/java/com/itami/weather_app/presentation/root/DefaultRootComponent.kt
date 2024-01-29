package com.itami.weather_app.presentation.root

import com.arkivanov.decompose.ComponentContext
import javax.inject.Inject

class DefaultRootComponent @Inject constructor(
    private val componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {

    

}