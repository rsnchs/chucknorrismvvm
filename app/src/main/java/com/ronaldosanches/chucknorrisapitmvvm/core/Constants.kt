package com.ronaldosanches.chucknorrisapitmvvm.core

object Constants {
    object AppConstraints {
        const val MIN_CLICK_INTERVAL = 1_000
        const val ALERT_INTERVAL = 3_000L
        const val BUFFER_CAPACITY = 3
    }

    object ApiUrls {
        const val BASE_URL = "https://api.chucknorris.io/jokes/"
        const val RANDOM_URL_MODIFIER = "random"
        const val CATEGORIES_URL_MODIFIER = "categories"
        const val SEARCH_URL_MODIFIER = "search"
        const val CATEGORY_PARAM = "category"
        const val QUERY_PARAM = "query"
    }

    object CustomAttributes {
        const val CATEGORY_ALL = "all"
    }

    object Sqlite {
        const val DB_NAME = "jokes-db"
        const val DB_VERSION = 1
        const val JOKES_TABLE = "jokes"
        const val LAST_VISUALIZED_TABLE = "last_visualized"
        const val ID_LAST_VISUALIZED = "custom_id_last_visualized_joke"
    }

    object ViewType {
        const val JOKES = 1
        const val GENERIC_ITEM_LIST = 2
        const val CATEGORIES = 3
        const val SECTION_TITLE = 4
        const val WARNING = 5
        const val LOADING = 6
    }

    object NightMode {
        const val LIGHT_MODE = 1
        const val DARK_MODE = 2
    }

    object Position {
        const val FIRST_POSITION = 0
        const val INVALID_POSITION = -1
    }

    object HttpParameters {
        const val MIMETYPE_TEXT = "text/plain"
    }
}