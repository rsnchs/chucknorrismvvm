package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.customviews

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.ronaldosanches.chucknorrisapitmvvm.R

@Composable
fun JokeCardContent(
    state: ChuckViewState = ChuckViewState.Normal,
    showLoadMore: Boolean = false,
    isJokeFavorite: Boolean = false,
    showLoadMoreAction : (() -> Unit)? = null,
) {
    Card(
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.card_corner_radius)),
        elevation = dimensionResource(id = R.dimen.card_elevation),
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.space_10dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.space_10dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when(state) {
                ChuckViewState.Empty -> JokeCardContentLoading(showLoadMore)
                ChuckViewState.Error -> JokeCardContentLoading(showLoadMore)
                ChuckViewState.Loading -> JokeCardContentLoading(showLoadMore)
                ChuckViewState.Normal -> JokeCardContentValid(showLoadMore, showLoadMoreAction, isJokeFavorite)
            }
        }
    }
}

@Composable
private fun JokeCardContentLoading(showLoadMore: Boolean) {
    LoadingShimmerEffect(showLoadMore = showLoadMore)
}

@Composable
private fun JokeCardContentValid(
    showLoadMore: Boolean = false,
    showLoadMoreAction: (() -> Unit)? = null,
    isJokeFavorite: Boolean,
) {
    Text(
        text = "Category: All",
        color = getColor(R.attr.colorPrimary),
        fontSize = dimensionResource(id = R.dimen.text_app_subtitle).value.sp,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
    )
    Text(
        text = "Texto da piada",
        color = getColor(R.attr.colorPrimaryVariant),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(id = R.dimen.space_15dp)),
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_share),
            contentDescription = null,
            modifier = Modifier.padding(end = dimensionResource(id = R.dimen.space_10dp), top = dimensionResource(id = R.dimen.space_10dp))
        )
        IconToggleButton(
            checked = isJokeFavorite,
            onCheckedChange = { },
        ) {
            if(isJokeFavorite) {
                Icon(painter = painterResource(id = R.drawable.ic_favorite_on),
                    contentDescription = null,
                    tint = getColor(color = R.attr.favoriteTrue)
                )
            } else {
                Icon(painter = painterResource(id = R.drawable.ic_favorite_off),
                    contentDescription = null,
                    tint = getColor(color = R.attr.favoriteFalse)
                )
            }
        }
    }

    if(showLoadMore) {
        TextButton(
            onClick = { showLoadMoreAction?.invoke() },
            colors = ButtonDefaults.textButtonColors(contentColor = getColor(R.attr.colorPrimary))
        ) {
            Text(text = "load another joke")
            Icon(painter = painterResource(id = R.drawable.ic_refresh), contentDescription = "")
        }
    }
}