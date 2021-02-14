package com.ronaldosanches.chucknorrisapitmvvm.domain.entities

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResponse(val total: Long?,
                          val result: List<JokeResponse>?)