package com.ronaldosanches.chucknorrisapitmvvm.core.custom

sealed class ChuckSearch {
    data class Valid(val text: String) : ChuckSearch()
    data class Invalid(val text: String) : ChuckSearch()
}
