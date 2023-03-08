package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.customviews

import android.annotation.SuppressLint
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.IconToggleButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ronaldosanches.chucknorrisapitmvvm.R
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ResultChuck
import com.ronaldosanches.chucknorrisapitmvvm.core.theme.ChuckNorrisApiTheme
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeResponse

@Composable
fun JokeCardContent(
    category: String?,
    jokeResponse: ResultChuck<out JokeResponse>?,
    showLoadMore: Boolean = false,
    showLoadMoreAction: (() -> Unit)? = null,
    shareJoke: ((JokeResponse) -> Unit),
    favoriteClick: (JokeResponse) -> Unit,
) {
    Card(
        shape = RoundedCornerShape(ChuckNorrisApiTheme.dimensions.cardCornerRadius),
        elevation = ChuckNorrisApiTheme.dimensions.cardElevation,
        backgroundColor = ChuckNorrisApiTheme.colors.cardBgColor,
        modifier = Modifier
            .fillMaxWidth()
            .padding(ChuckNorrisApiTheme.dimensions.defaultSpace)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ChuckNorrisApiTheme.dimensions.defaultSpace),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when(jokeResponse) {
                is ResultChuck.Error -> JokeCardContentLoading(showLoadMore)
                is ResultChuck.Loading -> JokeCardContentLoading(showLoadMore)
                is ResultChuck.Success -> JokeCardContentValid(category, jokeResponse.data, showLoadMore, showLoadMoreAction, shareJoke, favoriteClick)
                null -> JokeCardContentLoading(showLoadMore)
            }
        }
    }
}

@Composable
private fun JokeCardContentLoading(showLoadMore: Boolean) {
    ShimmerAnimate {
        ShimmerJokeCard(showLoadMore = showLoadMore, brush = it)
    }
}

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
private fun JokeCardContentValid(
    category: String?,
    jokeResponse: JokeResponse,
    showLoadMore: Boolean = false,
    showLoadMoreAction: (() -> Unit)? = null,
    shareJoke: (JokeResponse) -> Unit,
    favoriteClick: (JokeResponse) -> Unit,
) {
    if(category != null) {
        Text(
            text = stringResource(id = R.string.joke_category_title, (category)),
            color = ChuckNorrisApiTheme.colors.titleColor,
            style = ChuckNorrisApiTheme.typography.subtitle,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
    }
    Text(
        text = jokeResponse.value ?: String(),
        color = ChuckNorrisApiTheme.colors.textColor,
        style = ChuckNorrisApiTheme.typography.text,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = ChuckNorrisApiTheme.dimensions.defaultSpace),
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(onClick = { shareJoke(jokeResponse) }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_share),
                contentDescription = null,
                tint = ChuckNorrisApiTheme.colors.iconColor,
                modifier = Modifier.padding(
                    end = ChuckNorrisApiTheme.dimensions.defaultSpace,
                    top = ChuckNorrisApiTheme.dimensions.defaultSpace,
                )
            )

        }
        IconToggleButton(
            checked = jokeResponse.isFavorite,
            onCheckedChange = { favoriteClick(jokeResponse) },
        ) {

            val transition = updateTransition(jokeResponse.isFavorite, label = "Checked indicator")
            val tint by transition.animateColor(
                label = "Tint"
            ) { isChecked ->
                if(isChecked) ChuckNorrisApiTheme.colors.favoriteColor else ChuckNorrisApiTheme.colors.iconColor
            }

            val size by transition.animateDp(
                transitionSpec = {
                    if (false isTransitioningTo true) {
                        keyframes {
                            durationMillis = 250
                            30.dp at 0 with LinearOutSlowInEasing // for 0-15 ms
                            35.dp at 15 with FastOutLinearInEasing // for 15-75 ms
                            40.dp at 75 // ms
                            35.dp at 150 // ms
                        }
                    } else {
                        spring(stiffness = Spring.StiffnessVeryLow)
                    }
                },
                label = "Size"
            ) { 30.dp }

//            if(jokeResponse.isFavorite) {
//                Icon(painter = painterResource(id = R.drawable.ic_favorite_on),
//                    contentDescription = null,
//                    tint = ChuckNorrisApiTheme.colors.favoriteColor
//                )
//            } else {
//                Icon(painter = painterResource(id = R.drawable.ic_favorite_off),
//                    contentDescription = null,
//                    tint = ChuckNorrisApiTheme.colors.iconColor
//                )
//            }
            Icon(
                painter = if(jokeResponse.isFavorite) {
                    painterResource(id = R.drawable.ic_favorite_on)
                } else {
                    painterResource(id = R.drawable.ic_favorite_off)
                },
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size((size))
            )
        }
    }

    if(showLoadMore) {
        TextButton(
            onClick = { showLoadMoreAction?.invoke() },
            colors = ButtonDefaults.textButtonColors(contentColor = ChuckNorrisApiTheme.colors.iconColor)
        ) {
            Text(text = stringResource(id = R.string.random_joke_button_load_another_joke))
            Icon(painter = painterResource(id = R.drawable.ic_refresh), contentDescription = null)
        }
    }
}