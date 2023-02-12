package com.ronaldosanches.chucknorrisapitmvvm.data.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RoomWarnings
import com.ronaldosanches.chucknorrisapitmvvm.core.Constants
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeResponse
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
@Parcelize
@JsonClass(generateAdapter = true)
@Entity(tableName = Constants.Sqlite.LAST_VISUALIZED_TABLE)
data class LastVisualizedItem(
        //one item table
        @PrimaryKey
        @ColumnInfo(name = "itemId",defaultValue = Constants.Sqlite.ID_LAST_VISUALIZED)
        val itemId: String = Constants.Sqlite.ID_LAST_VISUALIZED,
        @Embedded val joke: JokeResponse) : Parcelable