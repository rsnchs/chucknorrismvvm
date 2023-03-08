package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.screens.jokeoptions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.ronaldosanches.chucknorrisapitmvvm.R
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ChuckSearch
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ResultChuck
import com.ronaldosanches.chucknorrisapitmvvm.core.platform.NetworkInfo
import com.ronaldosanches.chucknorrisapitmvvm.core.theme.ChuckNorrisApiTheme
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeOptionsMenu
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeResponse
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.base.BaseFragment
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.customviews.JokeCardContent
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.customviews.MenuItem
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.customviews.TopBarStatus
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.viewobjects.ShareJoke
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class JokeOptionsFragment : BaseFragment() {

    private val viewModel: JokeOptionsViewModel by viewModels()
    @Inject lateinit var networkInfo : NetworkInfo

    companion object {
        private const val KEY_SELECTED_CATEGORY = "selected_category_result_key"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ChuckNorrisApiTheme {
                    JokeOptionsScreen()
                }
            }
        }
    }

    private fun onThemeToggle() {
        rotateNightTheme { }
    }

    @Composable
    fun JokeOptionsScreen() {
        Box(
            Modifier
                .fillMaxSize()
                .background(ChuckNorrisApiTheme.colors.bgColor)
        ) {
            val bgImageRes = if(isSystemInDarkTheme()) R.drawable.bg_main_dark else R.drawable.bg_main
            Image(
                painter = painterResource(id = bgImageRes),
                contentDescription = null,
            )
            TopBarStatus(viewModel.jokeState.observeAsState().value, networkInfo.result.observeAsState().value, ::onThemeToggle)
            Column (modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(id = R.string.app_title),
                    style = ChuckNorrisApiTheme.typography.title,
                    color = ChuckNorrisApiTheme.colors.titleColor,
                    modifier = Modifier
                        .padding(
                            top = ChuckNorrisApiTheme.dimensions.jokeOptionsMargin,
                            start = ChuckNorrisApiTheme.dimensions.defaultSpace,
                        )
                        .wrapContentWidth(Alignment.Start)
                )
                Text(
                    text = stringResource(id = R.string.app_subtitle),
                    color = ChuckNorrisApiTheme.colors.textColor,
                    style = ChuckNorrisApiTheme.typography.subtitle,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ChuckNorrisApiTheme.dimensions.defaultSpace)
                        .wrapContentWidth(Alignment.Start)
                )

                JokeCardContent(
                    viewModel.updateUICategory().observeAsState().value,
                    viewModel.jokeState.observeAsState().value,
                    showLoadMore = true,
                    showLoadMoreAction = { viewModel.getRandomJokeByCategory(false) },
                    shareJoke = ::shareJoke,
                    favoriteClick = ::clickFavorite
                )

                CreateMenuOptions(viewModel.jokeOptionsMenu.observeAsState().value, ::handleMenuOptionsClick)

                CreateTextInput(viewModel.searchState.observeAsState().value, ::validateSearchQuery, ::searchJoke)
            }
        }
    }

    private fun searchJoke(query: String) {
        goToSearchScreen(query)
    }

    private fun validateSearchQuery(query: String) {
        viewModel.isSearchQueryValid(query)
    }

    private fun clickFavorite(joke: JokeResponse) {
        viewModel.handleFavoriteButtonClick()
    }

    private fun shareJoke(joke: JokeResponse) {
        viewModel.shareJoke().observe(viewLifecycleOwner) {
            ShareJoke(this).create(it).build()
        }
    }

    @Composable
    fun CreateMenuOptions(list: List<JokeOptionsMenu>?, onClickMenuItem: (JokeOptionsMenu) -> Unit) {

        list?.let {
            Card(
                shape = RoundedCornerShape(ChuckNorrisApiTheme.dimensions.cardCornerRadius),
                elevation = ChuckNorrisApiTheme.dimensions.cardElevation,
                backgroundColor = ChuckNorrisApiTheme.colors.cardBgColor,
                modifier = Modifier.padding(horizontal = ChuckNorrisApiTheme.dimensions.defaultSpace),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ChuckNorrisApiTheme.dimensions.defaultSpace)
                ) {
                    for((index, item) in list.withIndex()) {
                        MenuItem(text = stringResource(id = item.text)) {
                            onClickMenuItem.invoke(item)
                        }
                        if(index != list.size - 1) {
                            Divider(
                                color = ChuckNorrisApiTheme.colors.divisorColor,
                                thickness = ChuckNorrisApiTheme.dimensions.lineHeight
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun CreateTextInput(value: ChuckSearch?, onValueChange : (String) -> Unit, search: (String) -> Unit) {
        value ?: return
        Column {
            TextField(
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = ChuckNorrisApiTheme.colors.inputBgColor,
                    textColor = ChuckNorrisApiTheme.colors.textColor,
                    placeholderColor = ChuckNorrisApiTheme.colors.textColor,
                    trailingIconColor = ChuckNorrisApiTheme.colors.iconColor,
                    focusedIndicatorColor = ChuckNorrisApiTheme.colors.divisorColor,
                    cursorColor = ChuckNorrisApiTheme.colors.divisorColor,
                ),
                value = when(value) {
                    is ChuckSearch.Invalid -> value.text
                    is ChuckSearch.Valid -> value.text
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(ChuckNorrisApiTheme.dimensions.defaultSpace),
                onValueChange = { newValue -> onValueChange(newValue) },
                placeholder = { Text(text = stringResource(id = R.string.search_field_hint))},
                trailingIcon = {
                    when(value) {
                        is ChuckSearch.Invalid -> {
                            Icon(painterResource(id = R.drawable.ic_error),null, tint = ChuckNorrisApiTheme.colors.errorColor)
                        }
                        is ChuckSearch.Valid -> {
                            IconButton(onClick = { search(value.text) }) {
                                Icon(painter = painterResource(id = R.drawable.ic_search), contentDescription = null)
                            }
                        }
                    }
                }
            )
            if(value is ChuckSearch.Invalid) {
                Text(
                    text = "Pesquisa inválida",
                    color = ChuckNorrisApiTheme.colors.errorColor,
                    style = ChuckNorrisApiTheme.typography.caption,
                    modifier = Modifier.padding(start = ChuckNorrisApiTheme.dimensions.defaultSpaceBigger)
                )
            }
        }
    }

    override fun setupView() {
        viewModel.categoriesResponse.observe(viewLifecycleOwner) {
            it?.let {
                when(it) {
                    is ResultChuck.Error -> handleError.showError(it.error)
                    is ResultChuck.Loading -> Unit
                    is ResultChuck.Success -> handleSuccessCategoriesResponse(it.data)
                }
            }
        }

        setFragmentResultListener(KEY_SELECTED_CATEGORY) {
                requestKey, bundle -> viewModel.handleCategoryResult(requestKey,bundle)
            .observe(viewLifecycleOwner) {
                it?.let {
                    viewModel.updateCategory(it)
                    viewModel.getRandomJokeByCategory(false)
                }
            }
        }

        viewModel.jokeState.observe(viewLifecycleOwner) {
            if(it is ResultChuck.Success) {
                viewModel.updateLastJoke(it.data)
            }
        }

        viewModel.getRandomJokeByCategory(true)
        viewModel.createMenuOptions()
    }

    override fun onResume() {
        super.onResume()
        networkInfo.registerCallback()
        viewModel.checkIfJokeIsFavorite()
    }

    private fun handleMenuOptionsClick(item: JokeOptionsMenu) {
        when (item) {
            JokeOptionsMenu.CHOOSE_CATEGORY -> viewModel.getCategoriesFromApi()
            JokeOptionsMenu.SHOW_ALL_FAVORITE_JOKES -> goToFavoritesScreen()
        }
    }

    override fun onPause() {
        super.onPause()
        networkInfo.unregisterCallback()
    }

    private fun goToFavoritesScreen() {
        safeNavigate(R.id.jokeOptionsFragment,JokeOptionsFragmentDirections
            .actionJokeOptionsFragmentToJokesListFragment())
    }

    private fun goToSearchScreen(query: String) {
        safeNavigate(R.id.jokeOptionsFragment,JokeOptionsFragmentDirections
            .actionJokeOptionsFragmentToJokeSearchFragment(query))
    }

    private fun handleSuccessCategoriesResponse(categories: List<String>) {
        safeNavigate(R.id.jokeOptionsFragment,JokeOptionsFragmentDirections
            .actionJokeOptionsFragmentToJokesCategoriesFragment(categories.toTypedArray()))
        viewModel.resetCategories()
    }
}

