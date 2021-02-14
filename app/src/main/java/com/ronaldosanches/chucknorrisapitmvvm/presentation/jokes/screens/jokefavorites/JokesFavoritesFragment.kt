package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.screens.jokefavorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ResultChuck
import com.ronaldosanches.chucknorrisapitmvvm.databinding.FragmentJokesFavoritesBinding
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeResponse
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.adapters.DynamicListAdapter
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.adapters.IJokeClick
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.base.BaseFragment
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.viewobjects.ShareJoke
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class JokesFavoritesFragment : BaseFragment(), IJokeClick {

    private val viewModel : JokesListViewModel by viewModels()
    private lateinit var binding: FragmentJokesFavoritesBinding
    @Inject lateinit var listAdapter: DynamicListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentJokesFavoritesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun setupView() {
        binding.rvFavoriteJokesList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = listAdapter
        }

        viewModel.jokeList.observe(viewLifecycleOwner, {
            (binding.rvFavoriteJokesList.adapter as DynamicListAdapter).updateList(it)
        })

        when(val result = viewModel.showAllFavoriteJokes()) {
            is ResultChuck.Success -> handleSuccessList(result.data)
            is ResultChuck.Error -> handleError.showError(result.error)
            is ResultChuck.Loading -> Unit
        }
    }

    override fun favoriteClick(joke: JokeResponse, position: Int) {
        viewModel.removeFromFavories(joke).observe(viewLifecycleOwner, {
            when(it) {
                is ResultChuck.Success -> Unit
                is ResultChuck.Error -> handleError.showError(it.error)
                is ResultChuck.Loading -> Unit
            }
        })
    }

    override fun shareClick(joke: JokeResponse) {
        joke.value?.let { ShareJoke(requireParentFragment()).create(it).build() }
    }

    private fun handleSuccessList(data: LiveData<List<JokeResponse>>) {
        data.observe(viewLifecycleOwner, {
            viewModel.formatJokesList(it)
        })
    }
}