package com.itami.weather_app.presentation.search

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.itami.weather_app.domain.model.City
import com.itami.weather_app.utils.scope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultSearchComponent @AssistedInject constructor(
    private val searchStoreFactory: SearchStoreFactory,
    @Assisted("searchMode") private val searchMode: SearchMode,
    @Assisted("onNavigateBack") private val onNavigateBack: () -> Unit,
    @Assisted("onCitySavedToFavourite") private val onCitySavedToFavourite: () -> Unit,
    @Assisted("onOpenCityForecast") private val onOpenCityForecast: (City) -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext,
) : SearchComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { searchStoreFactory.create(searchMode) }

    init {
        scope.launch {
            store.labels.collect { label ->
                when (label) {
                    is SearchStore.Label.NavigateBack -> {
                        onNavigateBack()
                    }

                    is SearchStore.Label.OpenCityForecast -> {
                        onOpenCityForecast(label.city)
                    }

                    SearchStore.Label.CitySavedToFavourite -> {
                        onCitySavedToFavourite()
                    }
                }
            }
        }
    }

    override val state: StateFlow<SearchStore.State>
        get() = store.stateFlow

    override fun onCityClick(city: City) {
        store.accept(SearchStore.Intent.CityClick(city))
    }

    override fun onNavigateBackClick() {
        store.accept(SearchStore.Intent.BackClick)
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("searchMode") searchMode: SearchMode,
            @Assisted("onNavigateBack") onNavigateBack: () -> Unit,
            @Assisted("onCitySavedToFavourite") onCitySavedToFavourite: () -> Unit,
            @Assisted("onOpenCityForecast") onOpenCityForecast: (City) -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext,
        ): DefaultSearchComponent

    }
}