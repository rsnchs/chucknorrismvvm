package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.adapters

interface IPositionClick : ContextRetriever {
    fun positionClick(position: Int, text: String)
}