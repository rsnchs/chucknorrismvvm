package com.ronaldosanches.chucknorrisapitmvvm.domain.entities

import android.os.Parcelable
import androidx.room.*
import com.ronaldosanches.chucknorrisapitmvvm.core.Constants
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ViewType
import com.squareup.moshi.*
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
@Entity(tableName = Constants.Sqlite.JOKES_TABLE)
data class JokeResponse(
        val categories: List<String>?,

        @Json(name = "created_at")
        @ColumnInfo(name = "created_at")
        val createdAt: String?,

        @Json(name = "icon_url")
        @ColumnInfo(name = "icon_url")
        val iconURL: String?,

        @PrimaryKey
        val id: String,

        @Json(name = "updated_at")
        @ColumnInfo(name = "updated_at")
        val updatedAt: String?,

        val url: String?,

        val value: String?,

        val isFavorite : Boolean = false
        ) : ViewType, Parcelable {

    constructor(jokeResponse: JokeResponse, id: String
    ) : this(jokeResponse.categories,jokeResponse.createdAt, jokeResponse.iconURL, id,
            jokeResponse.updatedAt, jokeResponse.url, jokeResponse.value, jokeResponse.isFavorite)

    constructor(jokeResponse: JokeResponse, isFavorite: Boolean
    ) : this(jokeResponse.categories,jokeResponse.createdAt, jokeResponse.iconURL, jokeResponse.id,
        jokeResponse.updatedAt, jokeResponse.url, jokeResponse.value, isFavorite)

    override fun getViewType(): Int {
        return Constants.ViewType.JOKES
    }
}


