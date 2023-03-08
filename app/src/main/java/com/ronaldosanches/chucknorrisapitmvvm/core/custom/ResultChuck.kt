package com.ronaldosanches.chucknorrisapitmvvm.core.custom

import com.ronaldosanches.chucknorrisapitmvvm.core.custom.error.ErrorEntity

sealed class ResultChuck<T> {
    data class Success<T>(val data: T) : ResultChuck<T>()
    data class Error<T>(val error: ErrorEntity) : ResultChuck<T>()
    data class Loading<T>(val data: T? = null) : ResultChuck<T>()

    fun success() : T = (this as Success).data
}