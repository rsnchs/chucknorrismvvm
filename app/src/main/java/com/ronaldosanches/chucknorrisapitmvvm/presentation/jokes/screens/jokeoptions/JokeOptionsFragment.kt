package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.screens.jokeoptions

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ronaldosanches.chucknorrisapitmvvm.R
import com.ronaldosanches.chucknorrisapitmvvm.core.Constants
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ResultChuck
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.setOnSafeClickListener
import com.ronaldosanches.chucknorrisapitmvvm.core.platform.NetworkInfo
import com.ronaldosanches.chucknorrisapitmvvm.core.platform.NetworkStatus
import com.ronaldosanches.chucknorrisapitmvvm.databinding.FragmentJokeOptionsBinding
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.adapters.DynamicListAdapter
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.adapters.IPositionClick
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.base.BaseFragment
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.viewobjects.SearchTextWatcher
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.viewobjects.ShareJoke
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
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
        binding  = FragmentJokeOptionsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun setupView() {
        viewModel.createMenuOptions().observe(viewLifecycleOwner) {
            (binding.rvJokeOptionsOptionsList.adapter as DynamicListAdapter).addResList(it)
        }

        binding.rvJokeOptionsOptionsList.apply {
            this.layoutManager = LinearLayoutManager(requireContext())
            adapter = listAdapter
            addItemDecoration(DividerItemDecoration(requireContext(),
                (this.layoutManager as LinearLayoutManager).orientation))
        }

        binding.inJokeOptionsTopMenu.btTopMenuTheme.setOnSafeClickListener{ rotateNightTheme { showLoading() } }

        binding.tlJokeOptionsSearchLayout.setEndIconOnClickListener {
            binding.etJokeOptionsSearch.text?.length?.let {
                validateSearchQuery(it) {
                    goToSearchScreen(binding.etJokeOptionsSearch.text.toString())
                }
            }
        }

        binding.inJokeOptionsRandomJokeContent.inRandomJokeJokeContent.btJokeContentFavorite
            .setOnSafeClickListener {
                viewModel.handleFavoriteButtonClick().observe(viewLifecycleOwner) {
                    when (it) {
                        is ResultChuck.Loading -> showLoading()
                        is ResultChuck.Success -> {
                            hideLoading()
                            handleFavoriteSuccessfulResponse(it.data)
                        }
                        is ResultChuck.Error -> {
                            hideLoading()
                            handleError.showError(it.error)
                        }
                    }
                }
            }

        binding.inJokeOptionsRandomJokeContent.inRandomJokeJokeContent.btJokeContentShare
            .setOnSafeClickListener {
                viewModel.shareJoke().observe(viewLifecycleOwner) {
                    ShareJoke(this).create(it).build()
                }
            }

        binding.inJokeOptionsRandomJokeContent.btRandomJokeLoadMore.setOnSafeClickListener {
            loadJokeFromApi(false)
        }

        networkInfo.result.observe(viewLifecycleOwner) {
            binding.inJokeOptionsTopMenu.btTopMenuConnection.isSelected =
                it == NetworkStatus.CONNECTED
        }

        setFragmentResultListener(KEY_SELECTED_CATEGORY) {
                requestKey, bundle -> viewModel.handleCategoryResult(requestKey,bundle)
            .observe(viewLifecycleOwner) {
                it?.let {
                    viewModel.updateCategory(it)
                    loadJokeFromApi(false)
                }
            }
        }

        viewModel.updateUICategory().observe(viewLifecycleOwner) {
            binding.inJokeOptionsRandomJokeContent.tvRandomJokeTitle
                .text = getString(R.string.joke_category_title, it.replaceFirstChar { char -> char.uppercase() })
        }

        viewModel.updateUIJoke().observe(viewLifecycleOwner) {
            binding.inJokeOptionsRandomJokeContent.inRandomJokeJokeContent
                .tvJokeContentJoke.text = it.value
            binding.inJokeOptionsRandomJokeContent.inRandomJokeJokeContent
                .btJokeContentFavorite.isSelected = it.isFavorite
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
        binding.etJokeOptionsSearch.addTextChangedListener(searchTextWatcher)
    }

    private fun clearTextChangeListener() {
        binding.etJokeOptionsSearch.removeTextChangedListener(searchTextWatcher)
    }

    private fun showSearchFieldValid() {
        binding.tlJokeOptionsSearchLayout.error = null
    }

    private fun showSearchFieldInvalid() {
        binding.tlJokeOptionsSearchLayout.error = getString(R.string.error_too_short_search_query)
    }

    private fun showLoading() {
        binding.inJokeOptionsTopMenu.pbTopMenuLoading.apply {
            visibility = View.VISIBLE
            show()
        }

        binding.inJokeOptionsRandomJokeContent.apply {
            this.slRandomJokeShimmer.showShimmer(true)
            this.inRandomJokeJokeContent.tvJokeContentJoke.setBackgroundColor(ContextCompat
                .getColor(requireContext(), R.color.text_color_secondary))
            this.tvRandomJokeTitle.setBackgroundColor(ContextCompat
                .getColor(requireContext(), R.color.text_color_primary))
        }
        binding.inJokeOptionsRandomJokeContent.llRandomJokeContainer
            .children.forEach { it.isEnabled = false }
    }

    private fun hideLoading() {
        binding.inJokeOptionsTopMenu.pbTopMenuLoading.apply {
            visibility = View.GONE
            hide()
        }
        binding.inJokeOptionsRandomJokeContent.apply {
            this.slRandomJokeShimmer.hideShimmer()
            this.inRandomJokeJokeContent.tvJokeContentJoke.setBackgroundColor(Color.TRANSPARENT)
            this.tvRandomJokeTitle.setBackgroundColor(Color.TRANSPARENT)
        }
        binding.inJokeOptionsRandomJokeContent.llRandomJokeContainer
            .children.forEach { it.isEnabled = true }
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