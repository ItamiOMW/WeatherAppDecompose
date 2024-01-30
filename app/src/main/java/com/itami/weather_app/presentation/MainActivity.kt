package com.itami.weather_app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import com.itami.weather_app.presentation.root.DefaultRootComponent
import com.itami.weather_app.presentation.root.RootContent
import com.itami.weather_app.presentation.ui.theme.WeatherAppDecomposeTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var rootComponentFactory: DefaultRootComponent.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rootComponent = rootComponentFactory.create(componentContext = defaultComponentContext())
        setContent {
            WeatherAppDecomposeTheme {
                RootContent(component = rootComponent)
            }
        }
    }
}