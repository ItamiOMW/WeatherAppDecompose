package com.itami.weather_app.presentation.favourite

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.itami.weather_app.domain.model.City
import com.itami.weather_app.domain.usecase.GetFavouriteCitiesUseCase
import com.itami.weather_app.domain.usecase.GetWeatherUseCase
import com.itami.weather_app.domain.util.AppResponse
import com.itami.weather_app.presentation.favourite.FavouriteStore.Intent
import com.itami.weather_app.presentation.favourite.FavouriteStore.Label
import com.itami.weather_app.presentation.favourite.FavouriteStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface FavouriteStore : Store<Intent, State, Label> {

    data class State(
        val cityItems: List<CityItem>,
    ) {

        data class CityItem(
            val city: City,
            val weatherState: WeatherState,
        )

        sealed interface WeatherState {

            data object Initial : WeatherState

            data object Loading : WeatherState

            data object Error : WeatherState

            data class Loaded(
                val tempC: Float,
                val iconUrl: String,
            ) : WeatherState
        }

    }

    sealed interface Intent {

        data object SearchClick : Intent

        data object AddFavouriteClick : Intent

        data class CityClick(val city: City) : Intent

    }

    sealed interface Label {

        data object SearchClick : Label

        data object AddFavouriteClick : Label

        data class CityClick(val city: City) : Label

    }
}

class FavouriteStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getFavouriteCitiesUseCase: GetFavouriteCitiesUseCase,
    private val getWeatherUseCase: GetWeatherUseCase,
) {

    fun create(): FavouriteStore =
        object : FavouriteStore, Store<Intent, State, Label> by storeFactory.create(
            name = "FavouriteStore",
            initialState = State(cityItems = emptyList()),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {

        data class FavouriteCitiesLoaded(val cities: List<City>) : Action

    }

    private sealed interface Msg {

        data class FavouriteCitiesLoaded(val cities: List<City>) : Msg

        data class WeatherLoaded(
            val cityId: Int,
            val tempC: Float,
            val conditionIconUrl: String,
        ) : Msg

        data class WeatherLoading(
            val cityId: Int,
        ) : Msg

        data class WeatherLoadingError(
            val cityId: Int,
        ) : Msg

    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                getFavouriteCitiesUseCase().collect { cities ->
                    dispatch(Action.FavouriteCitiesLoaded(cities))
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.AddFavouriteClick -> {
                    publish(Label.AddFavouriteClick)
                }

                is Intent.CityClick -> {
                    publish(Label.CityClick(intent.city))
                }


                is Intent.SearchClick -> {
                    publish(Label.SearchClick)
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.FavouriteCitiesLoaded -> {
                    val cities = action.cities
                    dispatch(Msg.FavouriteCitiesLoaded(cities))
                    cities.forEach { city ->
                        scope.launch {
                            loadWeatherForCity(city = city)
                        }
                    }
                }
            }
        }

        private suspend fun loadWeatherForCity(city: City) {
            dispatch(Msg.WeatherLoading(cityId = city.id))
            when (val response = getWeatherUseCase(cityId = city.id)) {
                is AppResponse.Success -> {
                    dispatch(
                        Msg.WeatherLoaded(
                            cityId = city.id,
                            tempC = response.data.tempC,
                            conditionIconUrl = response.data.conditionIconUrl
                        )
                    )
                }

                is AppResponse.Failed -> {
                    dispatch(
                        Msg.WeatherLoadingError(
                            cityId = city.id
                        )
                    )
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.FavouriteCitiesLoaded -> {
                    copy(
                        cityItems = msg.cities.map { city ->
                            State.CityItem(
                                city = city,
                                weatherState = State.WeatherState.Initial
                            )
                        }
                    )
                }

                is Msg.WeatherLoaded -> {
                    copy(
                        cityItems = cityItems.map { cityItem ->
                            if (cityItem.city.id == msg.cityId) {
                                cityItem.copy(
                                    weatherState = State.WeatherState.Loaded(
                                        tempC = msg.tempC,
                                        iconUrl = msg.conditionIconUrl
                                    )
                                )
                            } else cityItem
                        }
                    )
                }

                is Msg.WeatherLoading -> {
                    copy(
                        cityItems = cityItems.map { cityItem ->
                            if (cityItem.city.id == msg.cityId) {
                                cityItem.copy(weatherState = State.WeatherState.Loading)
                            } else cityItem
                        }
                    )
                }

                is Msg.WeatherLoadingError -> {
                    copy(
                        cityItems = cityItems.map { cityItem ->
                            if (cityItem.city.id == msg.cityId) {
                                cityItem.copy(weatherState = State.WeatherState.Error)
                            } else cityItem
                        }
                    )
                }
            }
    }
}
