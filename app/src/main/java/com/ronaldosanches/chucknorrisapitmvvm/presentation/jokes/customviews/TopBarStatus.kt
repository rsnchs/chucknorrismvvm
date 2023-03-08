package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.customviews

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import com.ronaldosanches.chucknorrisapitmvvm.R
import com.ronaldosanches.chucknorrisapitmvvm.core.custom.ResultChuck
import com.ronaldosanches.chucknorrisapitmvvm.core.platform.NetworkStatus
import com.ronaldosanches.chucknorrisapitmvvm.core.theme.ChuckNorrisApiColors
import com.ronaldosanches.chucknorrisapitmvvm.core.theme.ChuckNorrisApiDimens
import com.ronaldosanches.chucknorrisapitmvvm.core.theme.ChuckNorrisApiTheme
import com.ronaldosanches.chucknorrisapitmvvm.domain.entities.JokeResponse

@Composable
fun TopBarStatus(isJokeLoading: ResultChuck<JokeResponse>?,
                 networkStatus: NetworkStatus?,
                 onThemeToggle: () -> Unit) {
    val networkColor = when(networkStatus) {
        NetworkStatus.CONNECTED -> Color.Green
        NetworkStatus.DISCONNECTED -> Color.Red
        NetworkStatus.DISCONNECTING -> Color.Red
        null -> ChuckNorrisApiTheme.colors.iconColor
    }
    val themeColor = if (isSystemInDarkTheme()) {
        Color(0xFF800080)
    } else {
        Color.Yellow
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(ChuckNorrisApiTheme.dimensions.defaultSpace),
        horizontalArrangement = Arrangement.End,
        ) {
        if(isJokeLoading is ResultChuck.Loading) {
            CircularProgressIndicator(modifier = Modifier.size(ChuckNorrisApiTheme.dimensions.iconSize))
        }
        Spacer(modifier = Modifier.size(ChuckNorrisApiTheme.dimensions.defaultSpaceBigger))
        Icon(
            painter = painterResource(id = R.drawable.ic_connection),
            contentDescription = null,
            tint = networkColor,
        )
        Spacer(modifier = Modifier.size(ChuckNorrisApiTheme.dimensions.defaultSpaceBigger))
        IconButton(
            onClick = { onThemeToggle() },
            modifier = Modifier.size(ChuckNorrisApiTheme.dimensions.iconSize),
            ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_dark_mode),
                contentDescription = null,
                tint = themeColor,
                )
        }
    }
}