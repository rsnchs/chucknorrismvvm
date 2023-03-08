package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.screens.jokecategories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ViewType
import com.ronaldosanches.chucknorrisapitmvvm.core.theme.ChuckNorrisApiTheme
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.base.BaseFragment
import com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.customviews.DynamicListItems
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JokesCategoriesFragment : BaseFragment() {

    companion object {
        private const val KEY_SELECTED_CATEGORY = "selected_category_result_key"
        private const val KEY_SELECTED_CATEGORY_STRING = "selected_category_string_value_key"
    }

    private val categoriesResponse : JokesCategoriesFragmentArgs by navArgs()
    private val viewModel : JokeCategoriesViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ChuckNorrisApiTheme {
                    CategoriesMenu(viewModel.categoriesResponse.observeAsState().value)
                }
            }
        }
    }

    override fun setupView() {
        viewModel.createCategoriesMenu(categoriesResponse.listCategories)
    }

    @Composable
    fun CategoriesMenu(value: List<ViewType>?) {
        value ?: return
        DynamicListItems(
            list = value,
            showDivider = true,
            menuItemClick = ::positionClick
        )
    }

    private fun positionClick(text: String) {
        setFragmentResult(KEY_SELECTED_CATEGORY, bundleOf(KEY_SELECTED_CATEGORY_STRING to text))
        findNavController().popBackStack()
    }
}
