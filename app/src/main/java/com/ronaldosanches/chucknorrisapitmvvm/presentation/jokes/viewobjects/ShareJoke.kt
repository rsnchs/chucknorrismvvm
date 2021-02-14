package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.viewobjects

import android.content.Intent
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import com.ronaldosanches.chucknorrisapitmvvm.R
import com.ronaldosanches.chucknorrisapitmvvm.core.Constants

class ShareJoke(private val fragment: Fragment) {

    private var intentShare : Intent? = null

    fun create(jokeText: String) : ShareJoke {
        intentShare = ShareCompat.IntentBuilder.from(fragment.requireActivity())
                .setType(Constants.HttpParameters.MIMETYPE_TEXT)
                .setText(fragment.getString(R.string.share_joke_wrapper,jokeText))
                .setChooserTitle(fragment.getString(R.string.share_title))
                .intent
        return this
    }

    fun build() : ShareJoke {
        fragment.startActivity(intentShare)
        return this
    }
}