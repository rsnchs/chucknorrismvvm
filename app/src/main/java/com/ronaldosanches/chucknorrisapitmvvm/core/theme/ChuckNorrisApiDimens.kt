package com.ronaldosanches.chucknorrisapitmvvm.core.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

open class ChuckNorrisApiDimens(
    val zeroSpace : Dp = 0.dp,
    val defaultSpace : Dp = 10.dp,
    val defaultSpaceBigger : Dp = 15.dp,
    val largeSpace : Dp = 20.dp,
    val extraLargeSpace : Dp = 30.dp,
    val extraExtraLargeSpace : Dp = 50.dp,
    val jokeOptionsMargin : Dp = 150.dp,
    val iconSize : Dp = 24.dp,
    val lineHeight : Dp = 1.dp,
    val cardCornerRadius : Dp = 2.dp,
    val cardElevation : Dp = 8.dp,
    val loadingSize : Dp = 50.dp,
    )

internal val LocalDimensions = staticCompositionLocalOf { ChuckNorrisApiDimens() }