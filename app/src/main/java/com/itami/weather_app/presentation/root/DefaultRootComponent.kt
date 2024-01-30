package com.itami.weather_app.presentation.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.itami.weather_app.domain.model.City
import com.itami.weather_app.presentation.details.DefaultDetailsComponent
import com.itami.weather_app.presentation.favourite.DefaultFavouriteComponent
import com.itami.weather_app.presentation.search.DefaultSearchComponent
import com.itami.weather_app.presentation.search.SearchMode
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.serialization.Serializable

class DefaultRootComponent @AssistedInject constructor(
    @Assisted("componentContext") componentContext: ComponentContext,
    private val detailsComponentFactory: DefaultDetailsComponent.Factory,
    private val searchComponentFactory: DefaultSearchComponent.Factory,
    private val favouriteComponentFactory: DefaultFavouriteComponent.Factory,
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.Favourite,
        handleBackButton = true,
        serializer = Config.serializer(),
        childFactory = ::child
    )

    private fun child(
        config: Config,
        componentContext: ComponentContext,
    ): RootComponent.Child {
        return when (config) {
            is Config.Details -> {
                val component = detailsComponentFactory.create(
                    city = config.city,
                    componentContext = componentContext,
                    onNavigateBack = {
                        navigation.pop()
                    },
                )
                RootComponent.Child.Details(component)
            }

            is Config.Favourite -> {
                val component = favouriteComponentFactory.create(
                    componentContext = componentContext,
                    onCityItemClicked = { city ->
                        navigation.push(Config.Details(city))
                    },
                    onSearchClicked = {
                        navigation.push(Config.Search(SearchMode.Regular))
                    },
                    onAddFavouriteClicked = {
                        navigation.push(Config.Search(SearchMode.AddToFavourite))
                    },
                )
                RootComponent.Child.Favourite(component)
            }

            is Config.Search -> {
                val component = searchComponentFactory.create(
                    searchMode = config.mode,
                    componentContext = componentContext,
                    onNavigateBack = {
                        navigation.pop()
                    },
                    onCitySavedToFavourite = {
                        navigation.pop()
                    },
                    onOpenCityForecast = { city ->
                        navigation.push(Config.Details(city))
                    },
                )
                RootComponent.Child.Search(component)
            }
        }
    }

    @Serializable
    sealed interface Config {

        @Serializable
        data object Favourite : Config

        @Serializable
        data class Details(val city: City) : Config

        @Serializable
        data class Search(val mode: SearchMode) : Config

    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
        ): DefaultRootComponent

    }

}