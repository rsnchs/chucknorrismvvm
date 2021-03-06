package com.ronaldosanches.chucknorrisapitmvvm.data.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ronaldosanches.chucknorrisapitmvvm.core.Constants
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ViewType

data class WarningItem(@StringRes val title: Int?, @DrawableRes val drawable: Int?) : ViewType {
    override fun getViewType(): Int = Constants.ViewType.WARNING

}