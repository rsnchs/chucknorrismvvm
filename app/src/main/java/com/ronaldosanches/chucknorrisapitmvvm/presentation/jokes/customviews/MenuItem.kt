package com.ronaldosanches.chucknorrisapitmvvm.presentation.jokes.customviews

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.ronaldosanches.chucknorrisapitmvvm.R

@Composable
fun MenuItem(@StringRes text: Int) {
    Text(
        text = stringResource(id = text),
        fontSize = dimensionResource(id = R.dimen.list_item).value.sp,
        color = getColor(color = R.attr.colorPrimary),
        modifier = Modifier.padding(dimensionResource(id = R.dimen.space_10dp))
    )
}