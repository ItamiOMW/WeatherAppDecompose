package com.itami.weather_app.presentation.favourite

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.itami.weather_app.domain.model.City
import com.itami.weather_app.presentation.root.scope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultFavouriteComponent @Inject constructor(
    private val componentContext: ComponentContext,
    private val favouriteStoreFactory: FavouriteStoreFactory,
    private val onCityItemClicked: (City) -> Unit,
    private val onSearchClicked: () -> Unit,
    private val onAddFavouriteClicked: () -> Unit,
) : FavouriteComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { favouriteStoreFactory.create() }

    init {
        scope.launch {
            store.labels.collect { label ->
                when (label) {
                    is FavouriteStore.Label.AddFavouriteClick -> {
                        onAddFavouriteClicked()
                    }

                    is FavouriteStore.Label.CityClick -> {
                        onCityItemClicked(label.city)
                    }

                    is FavouriteStore.Label.SearchClick -> {
                        onSearchClicked()
                    }
                }
            }
        }
    }

    override val state: StateFlow<FavouriteStore.State>
        get() = store.stateFlow

    override fun onSearchClick() {
        store.accept(FavouriteStore.Intent.SearchClick)
    }

    override fun onAddFavouriteClick() {
        store.accept(FavouriteStore.Intent.AddFavouriteClick)
    }

    override fun onCityItemClick(cityItem: City) {
        store.accept(FavouriteStore.Intent.CityClick(cityItem))
    }

}