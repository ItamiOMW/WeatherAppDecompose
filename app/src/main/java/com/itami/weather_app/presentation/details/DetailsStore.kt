package com.itami.weather_app.presentation.details

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.itami.weather_app.domain.model.City
import com.itami.weather_app.domain.model.Forecast
import com.itami.weather_app.domain.usecase.ChangeFavouriteUseCase
import com.itami.weather_app.domain.usecase.GetForecastUseCase
import com.itami.weather_app.domain.usecase.ObserveIsFavouriteUseCase
import com.itami.weather_app.domain.util.AppResponse
import com.itami.weather_app.presentation.details.DetailsStore.Intent
import com.itami.weather_app.presentation.details.DetailsStore.Label
import com.itami.weather_app.presentation.details.DetailsStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface DetailsStore : Store<Intent, State, Label> {

    data class State(
        val city: City,
        val isFavourite: Boolean,
        val forecastState: ForecastState,
    ) {

        sealed interface ForecastState {

            data object Initial : ForecastState

            data object Loading : ForecastState

            data object Error : ForecastState

            data class Loaded(
                val forecast: Forecast,
            ) : ForecastState
        }

    }

    sealed interface Intent {

        data object ChangeFavouriteClick : Intent

        data object BackClick : Intent

    }

    sealed interface Label {

        data object NavigateBack : Label

    }
}

class DetailsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val changeFavouriteUseCase: ChangeFavouriteUseCase,
    private val observeIsFavouriteUseCase: ObserveIsFavouriteUseCase,
    private val getForecastUseCase: GetForecastUseCase,
) {

    fun create(city: City): DetailsStore =
        object : DetailsStore, Store<Intent, State, Label> by storeFactory.create(
            name = "DetailsStore",
            initialState = State(
                city = city,
                isFavourite = false,
                forecastState = State.ForecastState.Initial
            ),
            bootstrapper = BootstrapperImpl(cityId = city.id),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {

        data class FavouriteChange(val isFavourite: Boolean) : Action

        data object ForecastLoading : Action

        data class ForecastLoaded(val forecast: Forecast) : Action

        data object ForecastError : Action

    }

    private sealed interface Msg {

        data class FavouriteChange(val isFavourite: Boolean) : Msg

        data object ForecastLoading : Msg

        data class ForecastLoaded(val forecast: Forecast) : Msg

        data object ForecastError : Msg

    }

    private inner class BootstrapperImpl(
        private val cityId: Int,
    ) : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                observeIsFavouriteUseCase.invoke(cityId = cityId).collect { isFavourite ->
                    dispatch(Action.FavouriteChange(isFavourite))
                }
            }
            scope.launch {
                dispatch(Action.ForecastLoading)
                when (val response = getForecastUseCase(cityId = cityId)) {
                    is AppResponse.Success -> {
                        dispatch(Action.ForecastLoaded(response.data))
                    }

                    is AppResponse.Failed -> {
                        dispatch(Action.ForecastError)
                    }
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.ChangeFavouriteClick -> {
                    scope.launch {
                        val city = getState().city
                        val isFavourite = getState().isFavourite
                        changeFavouriteUseCase(city = city, isFavouriteNow = isFavourite)
                    }
                }

                is Intent.BackClick -> {
                    publish(Label.NavigateBack)
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.FavouriteChange -> {
                    dispatch(Msg.FavouriteChange(isFavourite = action.isFavourite))
                }

                is Action.ForecastLoaded -> {
                    dispatch(Msg.ForecastLoaded(forecast = action.forecast))
                }

                is Action.ForecastError -> {
                    dispatch(Msg.ForecastError)
                }

                is Action.ForecastLoading -> {
                    dispatch(Msg.ForecastLoading)
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.FavouriteChange -> {
                    copy(isFavourite = msg.isFavourite)
                }

                is Msg.ForecastLoading -> {
                    copy(forecastState = State.ForecastState.Loading)
                }

                is Msg.ForecastLoaded -> {
                    copy(forecastState = State.ForecastState.Loaded(msg.forecast))
                }

                is Msg.ForecastError -> {
                    copy(forecastState = State.ForecastState.Error)
                }
            }
    }
}
