package com.itami.weather_app.presentation.details

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.itami.weather_app.presentation.details.DetailsStore.Intent
import com.itami.weather_app.presentation.details.DetailsStore.Label
import com.itami.weather_app.presentation.details.DetailsStore.State

internal interface DetailsStore : Store<Intent, State, Label> {

    sealed interface Intent {

    }

    data class State(val data: Unit)

    sealed interface Label {

    }
}

internal class DetailsStoreFactory(
    private val storeFactory: StoreFactory
) {

    fun create(): DetailsStore =
        object : DetailsStore, Store<Intent, State, Label> by storeFactory.create(
            name = "DetailsStore",
            initialState = State(Unit),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {

    }

    private sealed interface Msg {

    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {

        }

        override fun executeAction(action: Action, getState: () -> State) {

        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                else -> {
                    State(Unit)
                }
            }
    }
}
