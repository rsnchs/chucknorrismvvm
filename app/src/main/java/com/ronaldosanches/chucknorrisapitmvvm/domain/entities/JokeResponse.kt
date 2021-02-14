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
        var id: String,

        @Json(name = "updated_at")
        @ColumnInfo(name = "updated_at")
        val updatedAt: String?,

        val url: String?,

        val value: String?,

        var isFavorite : Boolean = false,

        @Ignore
        var isLoading : Boolean = false

        ) : ViewType, Parcelable {

    constructor(
        categories: List<String>?,
        createdAt: String?,
        iconURL: String?,
        id: String,
        updatedAt: String?,
        url: String?,
        value: String?,
        isFavorite: Boolean,
    ) : this(categories,createdAt,iconURL,id,updatedAt,url,value,isFavorite,false)

    constructor(jokeResponse: JokeResponse, id: String
    ) : this(jokeResponse.categories,jokeResponse.createdAt, jokeResponse.iconURL, id,
            jokeResponse.updatedAt, jokeResponse.url, jokeResponse.value, jokeResponse.isFavorite,
            jokeResponse.isLoading)

    constructor(jokeResponse: JokeResponse, isFavorite: Boolean
    ) : this(jokeResponse.categories,jokeResponse.createdAt, jokeResponse.iconURL, jokeResponse.id,
        jokeResponse.updatedAt, jokeResponse.url, jokeResponse.value, isFavorite,
        jokeResponse.isLoading)

    override fun getViewType(): Int {
        return Constants.ViewType.JOKES
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JokeResponse

        if (id != other.id) return false
        if (value != other.value) return false
        if (isFavorite != other.isFavorite) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (value?.hashCode() ?: 0)
        return result
    }
}


