package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.customviews

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.ronaldosanches.chucknorrisapitmvvm.R
import com.ronaldosanches.chucknorrisapitmvvm.core.Constants
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ResultChuck
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ViewType
import com.ronaldosanches.chucknorrisapitmvvm.core.theme.ChuckNorrisApiTheme
import com.ronaldosanches.chucknorrisapitmvvm.data.models.GenericListItem
import com.ronaldosanches.chucknorrisapitmvvm.data.models.SectionTitleItem
import com.ronaldosanches.chucknorrisapitmvvm.data.models.WarningItem
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeResponse

@Composable
fun DynamicListItems(
    list: List<ViewType>,
    showDivider: Boolean = false,
    menuItemClick: ((String) -> Unit)? = null,
    cardShareClick: ((JokeResponse) -> Unit)? = null,
    favoriteClick: ((JokeResponse) -> Unit)? = null,
    ) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ChuckNorrisApiTheme.colors.bgColor),
    ) {
        items(list.size) { index ->
            when(list[index].getViewType()) {
                Constants.ViewType.SECTION_TITLE -> {
                    DynamicListSectionTitle(
                        stringRes = (list[index] as SectionTitleItem).stringId
                    )
                }
                Constants.ViewType.GENERIC_ITEM_LIST -> {
                    (list[index] as GenericListItem).itemTitle?.let { text ->
                        MenuItem(text, menuItemClick)
                    }
                }
                Constants.ViewType.JOKES -> {
                    JokeCardContent(category = null,
                        showLoadMore = false,
                        jokeResponse = ResultChuck.Success(list[index] as JokeResponse),
                        shareJoke = { cardShareClick?.invoke(list[index] as JokeResponse) },
                        favoriteClick = { favoriteClick?.invoke(list[index] as JokeResponse) },
                        )
                }
                Constants.ViewType.WARNING -> {
                    val text = stringResource((list[index] as? WarningItem)?.title ?: R.string.no_item_found)
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(
                                    top = ChuckNorrisApiTheme.dimensions.largeSpace,
                                    start = ChuckNorrisApiTheme.dimensions.defaultSpace,
                                    end = ChuckNorrisApiTheme.dimensions.defaultSpace,
                                )
                                .align(Alignment.CenterHorizontally),
                            text = text,
                            style = ChuckNorrisApiTheme.typography.text,
                            color = ChuckNorrisApiTheme.colors.textColor,
                        )
                        Image(
                            modifier = Modifier
                                .padding(top = ChuckNorrisApiTheme.dimensions.largeSpace)
                                .align(Alignment.CenterHorizontally),
                            painter = painterResource(id = R.drawable.ic_nunchuck_norris_),
                            contentDescription = null
                        )
                    }
                }
            }
            if(showDivider && index < (list.size - 1)) {
                Divider(
                    color = ChuckNorrisApiTheme.colors.divisorColor,
                    thickness = ChuckNorrisApiTheme.dimensions.lineHeight
                )
            }
        }
    }
}

@Composable
fun DynamicListSectionTitle(@StringRes stringRes: Int) {
    Text(
        style = ChuckNorrisApiTheme.typography.title,
        text = stringResource(id = stringRes),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                bottom = ChuckNorrisApiTheme.dimensions.defaultSpaceBigger,
                top = ChuckNorrisApiTheme.dimensions.extraLargeSpace,
                start = ChuckNorrisApiTheme.dimensions.defaultSpace,
                end = ChuckNorrisApiTheme.dimensions.defaultSpace,
            ),
        color = ChuckNorrisApiTheme.colors.titleColor,
    )
}