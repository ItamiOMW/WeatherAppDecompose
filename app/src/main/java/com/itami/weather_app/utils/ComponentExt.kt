package com.itami.weather_app.utils

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

val ComponentContext.scope: CoroutineScope
    get() = CoroutineScope(
        context = Dispatchers.Main.immediate + SupervisorJob()
    ).apply { lifecycle.doOnDestroy { cancel() } }