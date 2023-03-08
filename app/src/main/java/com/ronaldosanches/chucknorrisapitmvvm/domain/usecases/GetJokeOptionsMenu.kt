package com.ronaldosanches.chucknorrisapitmvvm.domain.usecases

import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeOptionsMenu

class GetJokeOptionsMenu {
    operator fun invoke() : List<JokeOptionsMenu> {
        return listOf(
            JokeOptionsMenu.CHOOSE_CATEGORY,
            JokeOptionsMenu.SHOW_ALL_FAVORITE_JOKES,
        )
    }
}