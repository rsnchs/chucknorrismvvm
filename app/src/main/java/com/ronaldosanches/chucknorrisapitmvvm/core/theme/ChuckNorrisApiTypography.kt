package com.ronaldosanches.chucknorrisapitmvvm.core.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

data class ChuckNorrisApiTypography(
    val title: TextStyle = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
    ),
    val subtitle: TextStyle = TextStyle(
        fontSize = 18.sp
    ),
    val text : TextStyle = TextStyle(
        fontSize = 14.sp,
    ),
    val button : TextStyle = TextStyle(
        fontSize = 16.sp,
    ),
    val caption : TextStyle = TextStyle(
        fontSize =  16.sp
    )
)

internal val LocalTypography = staticCompositionLocalOf { ChuckNorrisApiTypography() }