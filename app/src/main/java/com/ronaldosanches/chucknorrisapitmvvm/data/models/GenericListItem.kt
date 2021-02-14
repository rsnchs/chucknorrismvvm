package com.ronaldosanches.chucknorrisapitmvvm.data.models

import androidx.annotation.StringRes
import com.ronaldosanches.chucknorrisapitmvvm.core.Constants
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ViewType

data class GenericListItem(@StringRes val itemTitleRes: Int?, val itemTitle: String?) : ViewType {
    override fun getViewType(): Int {
        return Constants.ViewType.GENERIC_ITEM_LIST
    }
}