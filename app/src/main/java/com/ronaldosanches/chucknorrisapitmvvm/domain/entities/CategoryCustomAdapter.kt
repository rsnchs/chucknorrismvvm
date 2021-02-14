@file:Suppress("unused")

package com.ronaldosanches.chucknorrisapitmvvm.domain.entities


import com.squareup.moshi.*

class CategoryCustomAdapter {

    @FromJson fun fromJson(reader: JsonReader) : CategoryResponse {
        val listCategories = mutableListOf<String>()

        reader.beginArray()
        while (reader.hasNext()) {
            listCategories.add(reader.nextString())
        }
        reader.endArray()

        return CategoryResponse(listCategories.toList())
    }

    @ToJson fun toJson(list: CategoryResponse) : List<String>? {
        return list.categories
    }
}