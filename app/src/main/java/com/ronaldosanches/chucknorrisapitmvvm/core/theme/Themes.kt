package com.ronaldosanches.chucknorrisapitmvvm.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember

object ChuckNorrisApiTheme {
    val colors : ChuckNorrisApiColors
    @Composable
    @ReadOnlyComposable
    get() = LocalColors.current

    val dimensions : ChuckNorrisApiDimens
    @Composable
    @ReadOnlyComposable
    get() = LocalDimensions.current

    val typography : ChuckNorrisApiTypography
    @Composable
    @ReadOnlyComposable
    get() = LocalTypography.current
}

@Composable
fun ChuckNorrisApiTheme(
    colors: ChuckNorrisApiColors = if(isSystemInDarkTheme()) darkColors() else lightColors(),
    dimensions: ChuckNorrisApiDimens = ChuckNorrisApiTheme.dimensions,
    typography: ChuckNorrisApiTypography = ChuckNorrisApiTheme.typography,
    content: @Composable () -> Unit,
) {
    val rememberedColors = remember { colors.copy() }.apply { updateColorsFrom(colors) }
    CompositionLocalProvider(
        LocalColors provides rememberedColors,
        LocalDimensions provides dimensions,
        LocalTypography provides typography,
    ) {
        content()
    }
}