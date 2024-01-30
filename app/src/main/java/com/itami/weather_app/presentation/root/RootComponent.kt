package com.itami.weather_app.presentation.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.itami.weather_app.presentation.details.DetailsComponent
import com.itami.weather_app.presentation.favourite.FavouriteComponent
import com.itami.weather_app.presentation.search.SearchComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {

        data class Favourite(val component: FavouriteComponent) : Child

        data class Details(val component: DetailsComponent) : Child

        data class Search(val component: SearchComponent) : Child

    }

}