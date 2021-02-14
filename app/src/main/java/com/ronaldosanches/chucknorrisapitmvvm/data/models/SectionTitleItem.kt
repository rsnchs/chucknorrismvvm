package com.ronaldosanches.chucknorrisapitmvvm.data.models

import com.ronaldosanches.chucknorrisapitmvvm.core.Constants
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ViewType

data class SectionTitleItem(val stringId: Int) : ViewType {
    override fun getViewType(): Int = Constants.ViewType.SECTION_TITLE
}