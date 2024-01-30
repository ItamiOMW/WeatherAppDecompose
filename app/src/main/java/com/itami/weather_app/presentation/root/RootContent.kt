package com.itami.weather_app.presentation.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.itami.weather_app.presentation.details.DetailsContent
import com.itami.weather_app.presentation.favourite.FavouriteContent
import com.itami.weather_app.presentation.search.SearchContent

@Composable
fun RootContent(component: RootComponent) {
    Children(stack = component.stack) {
        when (val instance = it.instance) {
            is RootComponent.Child.Details -> {
                DetailsContent(component = instance.component)
            }

            is RootComponent.Child.Favourite -> {
                FavouriteContent(component = instance.component)
            }

            is RootComponent.Child.Search -> {
                SearchContent(component = instance.component)
            }
        }
    }
}