package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.viewobjects

import com.ronaldosanches.chucknorrisapitmvvm.core.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class ShowErrorAlert(
    private val showMessage: () -> Unit,
    private val hideMessage: () -> Unit,
    private val updateText: (String) -> Unit,
    private val alertDuration: Long = Constants.AppConstraints.ALERT_INTERVAL,
    private val bufferCapacity: Int = Constants.AppConstraints.BUFFER_CAPACITY) {

    private val channel = Channel<String>()
    private var currentlyShowing : Boolean = false

    fun addNewMessage(message: String) {
        channel.trySend(message)
    }

    fun collect(coroutineScope: CoroutineScope) = coroutineScope.launch {
        channel.consumeAsFlow()
                .buffer(bufferCapacity)
                .collect {
                    if(!currentlyShowing) {
                        showMessage()
                        currentlyShowing = true
                    }
                    updateText(it)
                    delay(alertDuration)
                    if(currentlyShowing) {
                        hideMessage()
                        currentlyShowing = false
                    }
                }
    }
}