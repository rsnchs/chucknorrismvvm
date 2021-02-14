package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.screens.jokesearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ResultChuck
import com.ronaldosanches.chucknorrisapitmvvm.databinding.FragmentJokeSearchBinding
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeResponse
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.SearchResponse
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.adapters.DynamicListAdapter
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.adapters.IJokeClick
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.base.BaseFragment
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.viewobjects.ShareJoke
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class JokeSearchFragment : BaseFragment(), IJokeClick {

    private val viewModel : JokeSearchViewModel by viewModels()
    private val searchQuery : JokeSearchFragmentArgs by navArgs()
    @Inject lateinit var listAdapter: DynamicListAdapter
    private lateinit var binding : FragmentJokeSearchBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentJokeSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun setupView() {
        binding.rvJokeSearchList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = listAdapter
        }

        viewModel.jokeList.observe(viewLifecycleOwner, {
            (binding.rvJokeSearchList.adapter as DynamicListAdapter).updateList(it)
        })

        viewModel.getJokeBySearch(searchQuery.query).observe(viewLifecycleOwner, {
            handleSearchResponse(it)
        })
    }

    private fun handleSearchResponse(searchResponse: ResultChuck<SearchResponse>?) {
        viewModel.handleSearchResponse(searchResponse)
    }

    override fun favoriteClick(joke: JokeResponse, position: Int) {
        viewModel.handleFavoriteButtonClick(joke).observe(viewLifecycleOwner, {
            when(it) {
                is ResultChuck.Success -> updateAdapterPosition(it.data, joke.id,position)
                is ResultChuck.Error -> handleError.showError(it.error)
                is ResultChuck.Loading -> Unit
            }
        })
    }

    private fun updateAdapterPosition(data: Number, id: String, position: Int) {
        (binding.rvJokeSearchList.adapter as DynamicListAdapter)
                .updateForFavoriteStateChanged(data is Long, id, position)
    }

    override fun shareClick(joke: JokeResponse) {
        joke.value?.let {
            ShareJoke(requireParentFragment()).create(it).build()
        }
    }
}