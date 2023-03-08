package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.customviews

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ronaldosanches.chucknorrisapitmvvm.R

@Composable
fun ShimmerAnimate(view: @Composable (Brush) -> Unit){
    val gradient = listOf(
        Color.Gray.copy(alpha = 0.3f),
        Color.Gray.copy(alpha = 0.1f),
        Color.Gray.copy(alpha = 0.4f)
    )

    val transition = rememberInfiniteTransition()

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
    view.invoke(brush)
}

@Composable
fun ShimmerJokeCard(showLoadMore: Boolean, brush: Brush) {
    Spacer(
        modifier = Modifier
            .fillMaxWidth(fraction = 1f)
            .height(20.dp)
            .background(brush)
    )
    Spacer(modifier = Modifier.height(5.dp))
    Spacer(
        modifier = Modifier
            .fillMaxWidth(fraction = 1f)
            .height(30.dp)
            .background(brush)
    )
    Spacer(modifier = Modifier.height(10.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_share),
            tint = Color.Gray.copy(alpha = 0.7f),
            contentDescription = null,
            modifier = Modifier.background(brush)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_favorite_off),
            tint = Color.Gray.copy(alpha = 0.7f),
            contentDescription = null,
            modifier = Modifier.background(brush)
        )
    }

    if(showLoadMore) {
        Spacer(modifier = Modifier.width(10.dp))
        TextButton(
            modifier = Modifier.background(brush),
            onClick = { },
            colors = ButtonDefaults.textButtonColors(contentColor = Color.Gray.copy(alpha = 0.7f),)
        ) {
            Text(text = stringResource(id = R.string.random_joke_button_load_another_joke))
            Icon(painter = painterResource(id = R.drawable.ic_refresh), contentDescription = "")
        }
        Spacer(modifier = Modifier.width(10.dp))
    }
}