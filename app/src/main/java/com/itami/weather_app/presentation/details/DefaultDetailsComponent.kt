package com.itami.weather_app.presentation.details

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
class DefaultDetailsComponent @AssistedInject constructor(
    private val detailStoreFactory: DetailsStoreFactory,
    @Assisted("city") private val city: City,
    @Assisted("onNavigateBack") private val onNavigateBack: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext,
) : DetailsComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { detailStoreFactory.create(city = city) }

    init {
        scope.launch {
            store.labels.collect { label ->
                when (label) {
                    is DetailsStore.Label.NavigateBack -> {
                        onNavigateBack()
                    }
                }
            }
        }
    }

    override val state: StateFlow<DetailsStore.State>
        get() = store.stateFlow

    override fun onNavigateBackClick() {
        store.accept(DetailsStore.Intent.BackClick)
    }

    override fun onChangeFavouriteClick() {
        store.accept(DetailsStore.Intent.ChangeFavouriteClick)
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("city") city: City,
            @Assisted("onNavigateBack") onNavigateBack: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext,
        ): DefaultDetailsComponent

    }
}