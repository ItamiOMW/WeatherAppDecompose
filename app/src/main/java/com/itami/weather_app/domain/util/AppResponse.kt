package com.itami.weather_app.domain.util

sealed class AppResponse<out T> {

    data class Success<T>(val data: T) : AppResponse<T>()

    data class Failed<T>(val message: String?) : AppResponse<T>()

    companion object {

        fun <T> success(data: T) = Success(data)

        fun <T> failed(message: String? = null) = Failed<T>(message)

    }
}