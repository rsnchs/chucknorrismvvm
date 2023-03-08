package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.screens.jokesearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ResultChuck
import com.ronaldosanches.chucknorrisapitmvvm.core.theme.ChuckNorrisApiTheme
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeResponse
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.base.BaseFragment
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.customviews.DynamicListItems
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.viewobjects.ShareJoke
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JokeSearchFragment : BaseFragment() {

    private val viewModel : JokeSearchViewModel by viewModels()
    private val searchQuery : JokeSearchFragmentArgs by navArgs()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ChuckNorrisApiTheme {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = ChuckNorrisApiTheme.colors.bgColor)
                            .padding(
                                bottom = ChuckNorrisApiTheme.dimensions.largeSpace,
                            )
                    ) {
                        viewModel.jokeList.observeAsState().value?.let {
                            when (it) {
                                is ResultChuck.Success -> DynamicListItems(
                                    list = it.data,
                                    showDivider = false,
                                    cardShareClick = ::shareClick,
                                    favoriteClick = ::favoriteClick
                                )
                                is ResultChuck.Loading -> {
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .padding(top = ChuckNorrisApiTheme.dimensions.largeSpace)
                                            .size(size = ChuckNorrisApiTheme.dimensions.loadingSize)
                                            .align(alignment = Alignment.CenterHorizontally)
                                    )
                                }
                                else -> Unit
                            }
                        }
                    }
                }
            }
        }
    }

    override fun setupView() {
        viewModel.getJokeBySearch(searchQuery.query)
    }

    private fun favoriteClick(joke: JokeResponse) {
        viewModel.handleFavoriteButtonClick(joke)
    }

    private fun shareClick(joke: JokeResponse) {
        joke.value?.let {
            ShareJoke(requireParentFragment()).create(it).build()
        }
    }
}