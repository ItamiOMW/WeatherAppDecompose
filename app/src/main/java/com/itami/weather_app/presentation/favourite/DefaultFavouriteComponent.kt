package com.itami.weather_app.presentation.favourite

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.itami.weather_app.domain.model.City
import com.itami.weather_app.presentation.root.scope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultFavouriteComponent @AssistedInject constructor(
    private val favouriteStoreFactory: FavouriteStoreFactory,
    @Assisted("onCityItemClicked") private val onCityItemClicked: (City) -> Unit,
    @Assisted("onSearchClicked") private val onSearchClicked: () -> Unit,
    @Assisted("onAddFavouriteClicked") private val onAddFavouriteClicked: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext,
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

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("onCityItemClicked") onCityItemClicked: (City) -> Unit,
            @Assisted("onSearchClicked") onSearchClicked: () -> Unit,
            @Assisted("onAddFavouriteClicked") onAddFavouriteClicked: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext,
        ): DefaultFavouriteComponent

    }
}