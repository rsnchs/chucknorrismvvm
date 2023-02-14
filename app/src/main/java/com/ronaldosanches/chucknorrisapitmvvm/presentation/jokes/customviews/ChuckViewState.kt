package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.customviews

sealed class ChuckViewState {
    object Loading : ChuckViewState()
    object Normal : ChuckViewState()
    object Error : ChuckViewState()
    object Empty : ChuckViewState()
}
