package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.screens.jokefavorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ResultChuck
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ViewType
import com.ronaldosanches.chucknorrisapitmvvm.core.theme.ChuckNorrisApiTheme
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeResponse
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.base.BaseFragment
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.customviews.DynamicListItems
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.viewobjects.ShareJoke
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JokesFavoritesFragment : BaseFragment() {

    private val viewModel : JokesListViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ChuckNorrisApiTheme {
                    FavoriteScreen(viewModel.jokeList.observeAsState())
                }
            }
        }
    }

    override fun setupView() {
        viewModel.getFavoriteJokes()
    }

    private fun favoriteClick(joke: JokeResponse) {
        viewModel.removeFromFavorites(joke)
    }

    private fun shareClick(joke: JokeResponse) {
        joke.value?.let { ShareJoke(requireParentFragment()).create(it).build() }
    }

    @Composable
    private fun FavoriteScreen(value: State<ResultChuck<List<ViewType>>?>) {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(color = ChuckNorrisApiTheme.colors.bgColor)
            .padding(
                bottom = ChuckNorrisApiTheme.dimensions.largeSpace,
            )) {
            when (value.value) {
                is ResultChuck.Success -> DynamicListItems(
                    list = value.value!!.success(),
                    showDivider = false,
                    cardShareClick = ::shareClick,
                    favoriteClick = ::favoriteClick
                )
                is ResultChuck.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(top = ChuckNorrisApiTheme.dimensions.largeSpace)
                            .size(size = ChuckNorrisApiTheme.dimensions.loadingSize)
                            .align(alignment = Alignment.CenterHorizontally)
                    )
                }
                else -> Unit
            }
        }
    }
}