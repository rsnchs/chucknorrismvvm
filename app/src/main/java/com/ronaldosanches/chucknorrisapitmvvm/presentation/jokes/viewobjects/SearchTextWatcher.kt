package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.viewobjects

import android.text.Editable
import android.text.TextWatcher

class SearchTextWatcher(private val onChange : (length: Int) -> Unit) : TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        s?.length?.let { onChange(it) }
    }

    override fun afterTextChanged(s: Editable?) = Unit
}