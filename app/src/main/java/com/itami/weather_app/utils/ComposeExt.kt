package com.itami.weather_app.utils

import kotlin.math.roundToInt

fun Float.formatTempC(): String = "${roundToInt()}°C"