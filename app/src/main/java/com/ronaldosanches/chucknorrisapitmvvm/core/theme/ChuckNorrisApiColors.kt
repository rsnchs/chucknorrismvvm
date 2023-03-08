package com.ronaldosanches.chucknorrisapitmvvm.core.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

open class ChuckNorrisApiColors(
    textColor: Color,
    titleColor: Color,
    bgColor: Color,
    cardBgColor: Color,
    iconColor: Color,
    buttonColor: Color,
    favoriteColor: Color,
    inputBgColor: Color,
    divisorColor: Color,
    errorColor: Color,
    isLight: Boolean,
) {
    var textColor by mutableStateOf(textColor)
        private set
    var titleColor by mutableStateOf(titleColor)
        private set
    var bgColor by mutableStateOf(bgColor)
        private set
    var cardBgColor by mutableStateOf(cardBgColor)
        private set
    var iconColor by mutableStateOf(iconColor)
        private set
    var buttonColor by mutableStateOf(buttonColor)
        private set
    var favoriteColor by mutableStateOf(favoriteColor)
        private set
    var inputBgColor by mutableStateOf(inputBgColor)
        private set
    var divisorColor by mutableStateOf(divisorColor)
        private set
    var errorColor by mutableStateOf(errorColor)
        private set
    var isLight by mutableStateOf(isLight)
        private set

    fun copy(
        textColor : Color = this.textColor,
        titleColor : Color = this.titleColor,
        bgColor : Color = this.bgColor,
        cardBgColor : Color = this.cardBgColor,
        iconColor : Color = this.iconColor,
        buttonColor: Color = this.buttonColor,
        favoriteColor : Color = this.favoriteColor,
        inputBgColor : Color = this.inputBgColor,
        divisorColor: Color = this.divisorColor,
        errorColor: Color = this.errorColor,
        isLight : Boolean = this.isLight,
    ) : ChuckNorrisApiColors = ChuckNorrisApiColors(
        textColor,
        titleColor,
        bgColor,
        cardBgColor,
        iconColor,
        buttonColor,
        favoriteColor,
        inputBgColor,
        divisorColor,
        errorColor,
        isLight,
    )

    fun updateColorsFrom(other: ChuckNorrisApiColors) {
        textColor = other.textColor
        titleColor = other.titleColor
        bgColor = other.bgColor
        cardBgColor = other.cardBgColor
        iconColor = other.iconColor
        buttonColor = other.buttonColor
        favoriteColor = other.favoriteColor
        inputBgColor = other.inputBgColor
        divisorColor = other.divisorColor
        errorColor = other.errorColor
        isLight = other.isLight
    }
}

private val textColorLight = Color(0xFF505050)
private val titleColorLight = Color(0xFF202020)
private val bgColorLight = Color(0xFFEDEDED)
private val cardBgColorLight = Color(0xFFF8F8F8)
private val iconColorLight = Color(0xFF000000)
private val buttonColorLight = Color(0xFF000000)
private val inputBgColorLight = Color(0xFFDDDDDD)
private val divisorColorLight = Color(0xFFDDDDDD)

private val textColorDark = Color(0xFFDDDDDD)
private val titleColorDark = Color(0xFFE4E4E4)
private val bgColorDark = Color(0xFF323232)
private val cardBgColorDark = Color(0xFF626262)
private val iconColorDark = Color(0xFFFFFFFF)
private val buttonColorDark = Color(0xFFFFFFFF)
private val inputBgColorDark = Color(0xFF2F2F2F)
private val divisorColorDark = Color(0xFF2F2F2F)

private val favoriteColor = Color(0xFFFE20A5)
private val errorColor = Color(0xFFB00020)

fun lightColors() : ChuckNorrisApiColors = ChuckNorrisApiColors(
    textColor = textColorLight,
    titleColor = titleColorLight,
    bgColor = bgColorLight,
    cardBgColor = cardBgColorLight,
    iconColor = iconColorLight,
    buttonColor = buttonColorLight,
    favoriteColor = favoriteColor,
    inputBgColor = inputBgColorLight,
    divisorColor = divisorColorLight,
    errorColor = errorColor,
    isLight = true,
)

fun darkColors() : ChuckNorrisApiColors = ChuckNorrisApiColors(
    textColor = textColorDark,
    titleColor = titleColorDark,
    bgColor = bgColorDark,
    cardBgColor = cardBgColorDark,
    iconColor = iconColorDark,
    buttonColor = buttonColorDark,
    favoriteColor = favoriteColor,
    inputBgColor = inputBgColorDark,
    divisorColor = divisorColorDark,
    errorColor = errorColor,
    isLight = false,
)

val LocalColors = staticCompositionLocalOf { lightColors() }