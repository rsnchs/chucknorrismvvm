package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.viewobjects

import com.ronaldosanches.chucknorrisapitmvvm.core.custom.error.ErrorEntity
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.callbacks.IFragActivity

class HandleError(private val callback: IFragActivity) {

    fun showError(errorEntity: ErrorEntity) {
        when(errorEntity) {
            is ErrorEntity.Network -> callback.showAlert("An error occurred, might have to do with your internet")
            is ErrorEntity.CacheNotFound -> callback.showAlert("You're not connected and there's nothing around to show")
            ErrorEntity.NotFound -> callback.showAlert("Not found")
            ErrorEntity.AccessDenied -> callback.showAlert("Access Denied")
            ErrorEntity.ServiceUnavailable -> callback.showAlert("Service Unavailable")
            is ErrorEntity.GenericError -> handleGenericError(errorEntity)
            ErrorEntity.Unknown -> callback.showAlert("Error unknown")
        }
    }

    private fun handleGenericError(genericError: ErrorEntity.GenericError) {
        val code = genericError.code
        val message = genericError.message
        when(code) {
            404 -> callback.showAlert("What you were looking for was not found")
        }
    }
}