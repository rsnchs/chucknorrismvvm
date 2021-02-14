package com.ronaldosanches.chucknorrisapitmvvm.domain.entities

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class CategoryResponse(val categories: List<String>?) : Parcelable