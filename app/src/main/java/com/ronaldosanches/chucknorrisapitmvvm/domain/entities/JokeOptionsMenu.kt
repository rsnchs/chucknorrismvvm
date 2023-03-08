package com.ronaldosanches.chucknorrisapitmvvm.domain.entities

import androidx.annotation.StringRes
import com.ronaldosanches.chucknorrisapitmvvm.R

enum class JokeOptionsMenu(@StringRes val text: Int) {
    CHOOSE_CATEGORY(R.string.menu_choose_category),
    SHOW_ALL_FAVORITE_JOKES((R.string.menu_show_all_favorites))
}