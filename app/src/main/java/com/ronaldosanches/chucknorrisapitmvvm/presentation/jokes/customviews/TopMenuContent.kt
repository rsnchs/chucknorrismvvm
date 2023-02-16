package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.customviews

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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.ronaldosanches.chucknorrisapitmvvm.R

@Preview
@Composable
fun TopMenuContent() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.space_10dp)),
        horizontalArrangement = Arrangement.End,
        ) {
        CircularProgressIndicator(modifier = Modifier.size(dimensionResource(id = R.dimen.icon_size)))
        Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.space_15dp)))
        Icon(painter = painterResource(id = R.drawable.ic_connection), contentDescription = null)
        Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.space_15dp)))
        IconButton(
            onClick = {},
            modifier = Modifier.size(dimensionResource(id = R.dimen.icon_size)),
            ) {
            Icon(painter = painterResource(id = R.drawable.ic_dark_mode), contentDescription = null)
        }
    }
}