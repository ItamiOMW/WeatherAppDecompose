package com.itami.weather_app.presentation.search

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.itami.weather_app.domain.model.City
import com.itami.weather_app.domain.usecase.ChangeFavouriteUseCase
import com.itami.weather_app.domain.usecase.SearchCityUseCase
import com.itami.weather_app.domain.util.AppResponse
import com.itami.weather_app.presentation.search.SearchStore.Intent
import com.itami.weather_app.presentation.search.SearchStore.Label
import com.itami.weather_app.presentation.search.SearchStore.State
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

interface SearchStore : Store<Intent, State, Label> {

    data class State(
        val searchQuery: String,
        val mode: SearchMode,
        val searchState: SearchState,
    ) {

        sealed interface SearchState {

            data object Initial : SearchState

            data object Loading : SearchState

            data object Error : SearchState

            data object NotFound : SearchState

            data class Found(val cities: List<City>) : SearchState

        }

    }

    sealed interface Intent {

        data class SearchQueryChange(val searchQuery: String) : Intent

        data object BackClick : Intent

        data object SearchClick : Intent

        data class CityClick(val city: City) : Intent

    }

    sealed interface Label {

        data object NavigateBack : Label

        data object CitySavedToFavourite : Label

        data class OpenCityForecast(val city: City) : Label

    }
}

class SearchStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val searchCityUseCase: SearchCityUseCase,
    private val changeFavouriteUseCase: ChangeFavouriteUseCase,
) {

    fun create(searchMode: SearchMode): SearchStore =
        object : SearchStore, Store<Intent, State, Label> by storeFactory.create(
            name = "SearchStore",
            initialState = State(
                searchQuery = "",
                mode = searchMode,
                searchState = State.SearchState.Initial
            ),
            executorFactory = { ExecutorImpl(searchMode = searchMode) },
            reducer = ReducerImpl
        ) {}

    private sealed interface Action

    private sealed interface Msg {

        data class SearchQueryChange(val searchQuery: String) : Msg

        data class SearchStateFound(val cities: List<City>) : Msg

        data object SearchStateNotFound : Msg

        data object SearchStateLoading : Msg

        data object SearchStateError : Msg

    }


    private inner class ExecutorImpl(
        private val searchMode: SearchMode,
    ) : CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        private var searchJob: Job? = null

        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.BackClick -> {
                    publish(Label.NavigateBack)
                }

                is Intent.SearchClick -> {
                    searchJob?.cancel()
                    searchJob = scope.launch {
                        dispatch(Msg.SearchStateLoading)
                        val searchQuery = getState().searchQuery
                        when (val response = searchCityUseCase(query = searchQuery)) {
                            is AppResponse.Success -> {
                                val cities = response.data
                                if (cities.isEmpty()) {
                                    dispatch(Msg.SearchStateNotFound)
                                }
                                dispatch(Msg.SearchStateFound(cities = cities))
                            }

                            is AppResponse.Failed -> {
                                dispatch(Msg.SearchStateError)
                            }
                        }
                    }
                }

                is Intent.SearchQueryChange -> {
                    dispatch(Msg.SearchQueryChange(searchQuery = intent.searchQuery))
                }

                is Intent.CityClick -> {
                    when (searchMode) {
                        SearchMode.Regular -> {
                            publish(Label.OpenCityForecast(city = intent.city))
                        }

                        SearchMode.AddToFavourite -> {
                            scope.launch {
                                changeFavouriteUseCase.addToFavourite(city = intent.city)
                                publish(Label.CitySavedToFavourite)
                            }
                        }
                    }
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {}
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.SearchQueryChange -> {
                    copy(searchQuery = msg.searchQuery)
                }

                is Msg.SearchStateError -> {
                    copy(searchState = State.SearchState.Error)
                }

                is Msg.SearchStateFound -> {
                    copy(searchState = State.SearchState.Found(cities = msg.cities))
                }

                is Msg.SearchStateLoading -> {
                    copy(searchState = State.SearchState.Loading)
                }

                is Msg.SearchStateNotFound -> {
                    copy(searchState = State.SearchState.NotFound)
                }
            }
    }
}
