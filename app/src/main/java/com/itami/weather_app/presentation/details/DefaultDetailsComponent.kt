package com.itami.weather_app.presentation.details

import com.arkivanov.decompose.ComponentContext
import javax.inject.Inject

class DefaultDetailsComponent @Inject constructor(
    private val componentContext: ComponentContext,
) : DetailsComponent, ComponentContext by componentContext {


}