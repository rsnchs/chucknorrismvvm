package com.ronaldosanches.chucknorrisapitmvvm.data.models

import com.ronaldosanches.chucknorrisapitmvvm.core.Constants
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ViewType
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.error.ErrorEntity

data class ErrorItem(val error: ErrorEntity) : ViewType {
    override fun getViewType() = Constants.ViewType.ERROR
}
