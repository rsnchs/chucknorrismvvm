package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.activities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.ronaldosanches.chucknorrisapitmvvm.core.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(): ViewModel() {

    fun handleBackPressed(popBack: Boolean?) = liveData {
        if(popBack != null && popBack == false) {
            emit(Unit)
        }
    }

    fun getCurrentNextTheme(currentTheme: Int) = liveData {
        if(currentTheme == Constants.NightMode.LIGHT_MODE) {
            emit(Constants.NightMode.DARK_MODE)
        } else {
            emit(Constants.NightMode.LIGHT_MODE)
        }
    }

}