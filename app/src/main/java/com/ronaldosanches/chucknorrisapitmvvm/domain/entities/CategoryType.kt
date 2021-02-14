package com.ronaldosanches.chucknorrisapitmvvm.domain.entities

sealed class CategoryType {
    object CategoryAll  : CategoryType() {
        const val name = "All"
    }
    data class CategoryFromApi(val categoryName: String) : CategoryType()
}