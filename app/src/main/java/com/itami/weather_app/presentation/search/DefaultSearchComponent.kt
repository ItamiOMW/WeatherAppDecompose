package com.itami.weather_app.presentation.search

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
class DefaultSearchComponent @Inject constructor(
    private val componentContext: ComponentContext,
    private val searchStoreFactory: SearchStoreFactory,
    private val searchMode: SearchMode,
    private val onNavigateBack: () -> Unit,
    private val onCitySavedToFavourite: () -> Unit,
    private val onOpenCityForecast: (City) -> Unit,
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

}