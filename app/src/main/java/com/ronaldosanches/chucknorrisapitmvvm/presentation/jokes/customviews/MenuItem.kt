package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.customviews

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.ronaldosanches.chucknorrisapitmvvm.R
import com.ronaldosanches.chucknorrisapitmvvm.core.theme.ChuckNorrisApiTheme
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeOptionsMenu

@Composable
fun MenuItem(text: String, click: ((String) -> Unit)?) {
    TextButton(onClick = { click?.invoke(text) }) {
        Text(
            text = text,
            style = ChuckNorrisApiTheme.typography.button,
            color = ChuckNorrisApiTheme.colors.buttonColor,
            modifier = Modifier
                .fillMaxWidth()
                .padding(ChuckNorrisApiTheme.dimensions.defaultSpace)
        )
    }
}