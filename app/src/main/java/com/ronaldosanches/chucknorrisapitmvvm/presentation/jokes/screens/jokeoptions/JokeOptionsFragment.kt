package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.screens.jokeoptions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.ronaldosanches.chucknorrisapitmvvm.R
import com.ronaldosanches.chucknorrisapitmvvm.core.Constants
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ResultChuck
import com.ronaldosanches.chucknorrisapitmvvm.core.platform.NetworkInfo
import com.ronaldosanches.chucknorrisapitmvvm.databinding.FragmentJokeOptionsBinding
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.adapters.DynamicListAdapter
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.adapters.IPositionClick
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.base.BaseFragment
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.customviews.JokeCardContent
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.customviews.MenuItem
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.customviews.getColor
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.viewobjects.SearchTextWatcher
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class JokeOptionsFragment : BaseFragment(), IPositionClick {

    private lateinit var binding: FragmentJokeOptionsBinding
    private val viewModel: JokeOptionsViewModel by viewModels()
    private val searchTextWatcher by lazy { SearchTextWatcher(::validateSearchQuery) }
    @Inject lateinit var listAdapter : DynamicListAdapter
    @Inject lateinit var networkInfo : NetworkInfo

    companion object {
        private const val KEY_SELECTED_CATEGORY = "selected_category_result_key"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding  = FragmentJokeOptionsBinding.inflate(layoutInflater).apply {
            composeView.setContent {
                MaterialTheme {
                    HeaderOptions()
                }
            }
        }

        return binding.root
    }

    @Composable
    fun HeaderOptions() {
        Box(Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.bg_main),
                contentDescription = "",
            )
            Column (modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(id = R.string.app_title),
                    fontSize = dimensionResource(id = R.dimen.text_app_title).value.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = dimensionResource(id = R.dimen.space_150dp),
                            start = dimensionResource(id = R.dimen.app_outside_margin)
                        )
                        .wrapContentWidth(Alignment.Start)
                )
                Text(
                    text = stringResource(id = R.string.app_subtitle),
                    color = colorResource(id = R.color.text_color_secondary),
                    fontSize = dimensionResource(id = R.dimen.text_app_subtitle).value.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = dimensionResource(id = R.dimen.app_outside_margin),
                            end = dimensionResource(id = R.dimen.app_outside_margin)
                        )
                        .wrapContentWidth(Alignment.Start)
                )
                JokeCardContent(
                    showLoadMore = true,
                    showLoadMoreAction = { loadJokeFromApi(false)},
                    isJokeFavorite = false
                )

                CreateMenuOptions(listOf(R.string.app_title, R.string.app_subtitle))

                CreateTextInput()
            }
        }
    }

    @Composable
    fun CreateMenuOptions(list: List<Int>) {
        Card(
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.card_corner_radius)),
            elevation = dimensionResource(id = R.dimen.card_elevation),
            modifier = Modifier.padding(dimensionResource(id = R.dimen.space_10dp))
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.space_10dp))
            ) {
                for((index, item) in list.withIndex()) {
                    MenuItem(text = item)
                    if(index != list.size - 1) {
                        Divider(
                            color = getColor(color = R.attr.primaryLight),
                            thickness = dimensionResource(id = R.dimen.line_height)
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun CreateTextInput() {
        TextField(
            value = "valor",
            onValueChange = {}
        )
    }

    override fun setupView() {
//        viewModel.createMenuOptions().observe(viewLifecycleOwner) {
//            (binding.rvJokeOptionsOptionsList.adapter as DynamicListAdapter).addResList(it)
//        }

//        binding.rvJokeOptionsOptionsList.apply {
//            this.layoutManager = LinearLayoutManager(requireContext())
//            adapter = listAdapter
//            addItemDecoration(DividerItemDecoration(requireContext(),
//                (this.layoutManager as LinearLayoutManager).orientation))
//        }

//        binding.inJokeOptionsTopMenu.btTopMenuTheme.setOnSafeClickListener{ rotateNightTheme { showLoading() } }

//        binding.tlJokeOptionsSearchLayout.setEndIconOnClickListener {
//            binding.etJokeOptionsSearch.text?.length?.let {
//                validateSearchQuery(it) {
//                    goToSearchScreen(binding.etJokeOptionsSearch.text.toString())
//                }
//            }
//        }

//        binding.inJokeOptionsRandomJokeContent.inRandomJokeJokeContent.btJokeContentFavorite
//            .setOnSafeClickListener {
//                viewModel.handleFavoriteButtonClick().observe(viewLifecycleOwner) {
//                    when (it) {
//                        is ResultChuck.Loading -> showLoading()
//                        is ResultChuck.Success -> {
//                            hideLoading()
//                            handleFavoriteSuccessfulResponse(it.data)
//                        }
//                        is ResultChuck.Error -> {
//                            hideLoading()
//                            handleError.showError(it.error)
//                        }
//                    }
//                }
//            }

//        binding.inJokeOptionsRandomJokeContent.inRandomJokeJokeContent.btJokeContentShare
//            .setOnSafeClickListener {
//                viewModel.shareJoke().observe(viewLifecycleOwner) {
//                    ShareJoke(this).create(it).build()
//                }
//            }

//        binding.inJokeOptionsRandomJokeContent.btRandomJokeLoadMore.setOnSafeClickListener {
//            loadJokeFromApi(false)
//        }

//        networkInfo.result.observe(viewLifecycleOwner) {
//            binding.inJokeOptionsTopMenu.btTopMenuConnection.isSelected =
//                it == NetworkStatus.CONNECTED
//        }

        setFragmentResultListener(KEY_SELECTED_CATEGORY) {
                requestKey, bundle -> viewModel.handleCategoryResult(requestKey,bundle)
            .observe(viewLifecycleOwner) {
                it?.let {
                    viewModel.updateCategory(it)
                    loadJokeFromApi(false)
                }
            }
        }

//        viewModel.updateUICategory().observe(viewLifecycleOwner) {
//            binding.inJokeOptionsRandomJokeContent.tvRandomJokeTitle
//                .text = getString(R.string.joke_category_title, it.replaceFirstChar { char -> char.uppercase() })
//        }

        viewModel.updateUIJoke().observe(viewLifecycleOwner) {
//            binding.inJokeOptionsRandomJokeContent.inRandomJokeJokeContent
//                .tvJokeContentJoke.text = it.value
//            binding.inJokeOptionsRandomJokeContent.inRandomJokeJokeContent
//                .btJokeContentFavorite.isSelected = it.isFavorite
        }

        loadJokeFromApi(true)
    }

    override fun onResume() {
        super.onResume()
        networkInfo.registerCallback()
        startTextChangeListener()
        viewModel.checkIfJokeIsFavorited()
    }

    override fun positionClick(position: Int, text: String) {
        when(position) {
            Constants.Position.FIRST_POSITION -> {
                viewModel.getCategoriesFromApi().observe(viewLifecycleOwner) {
                    when (it) {
                        is ResultChuck.Success -> {
                            hideLoading()
                            handleSuccessCategoriesResponse(it.data)
                        }
                        is ResultChuck.Error -> {
                            hideLoading()
                            handleError.showError(it.error)
                        }
                        is ResultChuck.Loading -> showLoading()
                    }
                }
            }
            else -> goToFavoritesScreen()
        }
    }

    override fun onPause() {
        super.onPause()
        networkInfo.unregisterCallback()
        clearTextChangeListener()
    }

    private fun loadJokeFromApi(searchForStateSaved: Boolean) {
        viewModel.getRandomJokeByCategory(searchForStateSaved).observe(viewLifecycleOwner) {
            when (it) {
                is ResultChuck.Loading -> showLoading()
                is ResultChuck.Success -> {
                    hideLoading()
                    viewModel.updateLastJoke(it.data)
                }
                is ResultChuck.Error -> {
                    hideLoading()
                    handleError.showError(it.error)
                }
            }
        }
    }

    private fun handleFavoriteSuccessfulResponse(result: Number) {
        viewModel.updateFavoriteState(result is Long)
    }

    private fun validateSearchQuery(length: Int, func : (() -> Unit)? = null) =
        viewModel.isSearchQueryValid(length).observe(viewLifecycleOwner) {
            when (it) {
                false -> showSearchFieldInvalid()
                true -> {
                    showSearchFieldValid()
                    func?.invoke()
                }
            }
        }

    private fun startTextChangeListener() {
//        binding.etJokeOptionsSearch.addTextChangedListener(searchTextWatcher)
    }

    private fun clearTextChangeListener() {
//        binding.etJokeOptionsSearch.removeTextChangedListener(searchTextWatcher)
    }

    private fun showSearchFieldValid() {
//        binding.tlJokeOptionsSearchLayout.error = null
    }

    private fun showSearchFieldInvalid() {
//        binding.tlJokeOptionsSearchLayout.error = getString(R.string.error_too_short_search_query)
    }

    private fun showLoading() {
//        binding.inJokeOptionsTopMenu.pbTopMenuLoading.apply {
//            visibility = View.VISIBLE
//            show()
//        }

//        binding.inJokeOptionsRandomJokeContent.apply {
//            this.slRandomJokeShimmer.showShimmer(true)
//            this.inRandomJokeJokeContent.tvJokeContentJoke.setBackgroundColor(ContextCompat
//                .getColor(requireContext(), R.color.text_color_secondary))
//            this.tvRandomJokeTitle.setBackgroundColor(ContextCompat
//                .getColor(requireContext(), R.color.text_color_primary))
//        }
//        binding.inJokeOptionsRandomJokeContent.llRandomJokeContainer
//            .children.forEach { it.isEnabled = false }
    }

    private fun hideLoading() {
//        binding.inJokeOptionsTopMenu.pbTopMenuLoading.apply {
//            visibility = View.GONE
//            hide()
//        }
//        binding.inJokeOptionsRandomJokeContent.apply {
//            this.slRandomJokeShimmer.hideShimmer()
//            this.inRandomJokeJokeContent.tvJokeContentJoke.setBackgroundColor(Color.TRANSPARENT)
//            this.tvRandomJokeTitle.setBackgroundColor(Color.TRANSPARENT)
//        }
//        binding.inJokeOptionsRandomJokeContent.llRandomJokeContainer
//            .children.forEach { it.isEnabled = true }
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
    }
}

