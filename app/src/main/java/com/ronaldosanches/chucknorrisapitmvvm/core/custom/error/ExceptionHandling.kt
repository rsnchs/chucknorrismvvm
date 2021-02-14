package com.ronaldosanches.chucknorrisapitmvvm.core.custom.error

import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ResultChuck
import com.squareup.moshi.Moshi
import retrofit2.HttpException
import java.io.IOException

fun <T> Throwable.handleNetworkError() : ResultChuck.Error<T> {
    return when(this) {
        is IOException -> ResultChuck.Error(error = ErrorEntity.Network)
        is UserIsOfflineException -> ResultChuck.Error(error = ErrorEntity.Network)
        is CacheException -> ResultChuck.Error(error = ErrorEntity.CacheNotFound)
        is HttpException -> {
            val code = this.code()
            val errorResponse = this.convertErrorBody()
            ResultChuck.Error(error = ErrorEntity.GenericError(code,errorResponse))
        }
        else -> ResultChuck.Error(error = ErrorEntity.Unknown)
    }
}

fun HttpException.convertErrorBody(): ErrorResponse? {
    return try {
        this.response()?.errorBody()?.source()?.let {
            val moshiAdapter = Moshi.Builder().build().adapter(ErrorResponse::class.java)
            moshiAdapter.fromJson(it)
        }
    } catch (exception: Exception) {
        null
    }
}