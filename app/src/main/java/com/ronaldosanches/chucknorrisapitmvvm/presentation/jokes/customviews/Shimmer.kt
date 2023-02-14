package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.customviews

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.ronaldosanches.chucknorrisapitmvvm.R

@Composable
fun LoadingShimmerEffect(showLoadMore: Boolean){
    val gradient = listOf(
        Color.LightGray.copy(alpha = 0.9f), //darker grey (90% opacity)
        Color.LightGray.copy(alpha = 0.3f), //lighter grey (30% opacity)
        Color.LightGray.copy(alpha = 0.9f)
    )

    val transition = rememberInfiniteTransition() // animate infinite times

    val translateAnimation = transition.animateFloat( //animate the transition
        initialValue = 0f,
        targetValue = 800f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 800, // duration for the animation
                easing = FastOutLinearInEasing
            )
        )
    )
    val brush = linearGradient(
        colors = gradient,
        start = Offset(200f, 200f),
        end = Offset(x = translateAnimation.value,
            y = translateAnimation.value)
    )
    ShimmerJokeCard(showLoadMore, brush = brush)
}

@Composable
fun ShimmerJokeCard(showLoadMore: Boolean, brush: Brush) {
    Spacer(
        modifier = Modifier
            .fillMaxWidth(fraction = 0.7f)
            .height(30.dp)
            .background(brush)
    )
    Spacer(modifier = Modifier.height(20.dp))
    Spacer(
        modifier = Modifier
            .fillMaxWidth(fraction = 0.9f)
            .height(60.dp)
            .background(brush)
    )
    Spacer(modifier = Modifier.height(20.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Spacer(
            modifier = Modifier
                .height(dimensionResource(id = R.dimen.icon_size))
                .width(dimensionResource(id = R.dimen.icon_size))
                .background(brush),
        )
        Spacer(modifier = Modifier.width(10.dp))
        Spacer(
            modifier = Modifier
                .height(dimensionResource(id = R.dimen.icon_size))
                .width(dimensionResource(id = R.dimen.icon_size))
                .background(brush),
        )
    }

    if(showLoadMore) {
        Spacer(modifier = Modifier.width(10.dp))
        Spacer(
            modifier = Modifier
                .fillMaxWidth(fraction = 0.6f)
                .height(20.dp)
                .background(brush)
        )
        Spacer(modifier = Modifier.width(10.dp))
    }
}