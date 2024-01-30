package com.itami.weather_app.presentation.details

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
class DefaultDetailsComponent @Inject constructor(
    private val componentContext: ComponentContext,
    private val detailStoreFactory: DetailsStoreFactory,
    private val city: City,
    private val onNavigateBack: () -> Unit,
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

}