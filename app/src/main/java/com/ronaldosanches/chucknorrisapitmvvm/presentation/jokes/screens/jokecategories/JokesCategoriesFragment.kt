package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.screens.jokecategories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ronaldosanches.chucknorrisapitmvvm.databinding.FragmentJokeCategoriesBinding
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.adapters.DynamicListAdapter
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.adapters.IPositionClick
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class JokesCategoriesFragment : BaseFragment(), IPositionClick {

    companion object {
        private const val KEY_SELECTED_CATEGORY = "selected_category_result_key"
        private const val KEY_SELECTED_CATEGORY_STRING = "selected_category_string_value_key"
    }

    private val categoriesResponse : JokesCategoriesFragmentArgs by navArgs()
    private lateinit var binding: FragmentJokeCategoriesBinding
    @Inject lateinit var listAdapter : DynamicListAdapter
    private val viewModel : JokeCategoriesViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentJokeCategoriesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun setupView() {
        binding.rvCategoriesLayout.apply {
            this.layoutManager = LinearLayoutManager(requireContext())
            this.adapter = listAdapter
            addItemDecoration(DividerItemDecoration(requireContext(),
                (layoutManager as LinearLayoutManager).orientation))
        }

        viewModel.createCategoriesMenu(categoriesResponse.listCategories).observe(viewLifecycleOwner, {
            (binding.rvCategoriesLayout.adapter as DynamicListAdapter).updateList(it)
        })
    }

    override fun positionClick(position: Int, text: String) {
        setFragmentResult(KEY_SELECTED_CATEGORY, bundleOf(KEY_SELECTED_CATEGORY_STRING to text))
        findNavController().popBackStack()
    }
}
