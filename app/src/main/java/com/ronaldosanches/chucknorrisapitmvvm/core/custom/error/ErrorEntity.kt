package com.ronaldosanches.chucknorrisapitmvvm.core.custom.error

sealed class ErrorEntity {

    object Network : ErrorEntity()

    object NotFound : ErrorEntity()

    object CacheNotFound : ErrorEntity()

    object AccessDenied : ErrorEntity()

    object ServiceUnavailable : ErrorEntity()

    class GenericError(val code: Int, val message: ErrorResponse? = null) : ErrorEntity() {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            return true
        }

        override fun hashCode(): Int {
            return javaClass.hashCode()
        }
    }

    object Unknown : ErrorEntity()
}