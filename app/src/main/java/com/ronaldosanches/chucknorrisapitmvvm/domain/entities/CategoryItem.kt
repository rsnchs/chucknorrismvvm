package com.ronaldosanches.chucknorrisapitmvvm.domain.entities

import android.os.Parcelable
import com.ronaldosanches.chucknorrisapitmvvm.core.Constants
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ViewType
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class CategoryItem(val category: String?) : Parcelable, ViewType {
    override fun getViewType(): Int = Constants.ViewType. CATEGORIES

}