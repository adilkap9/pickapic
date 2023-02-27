package com.example.pickapic.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.res.stringResource
import com.example.pickapic.R
import com.example.pickapic.ui.composables.TitleCard
import com.example.pickapic.ui.theme.Pencil700
import com.example.pickapic.ui.theme.PickapicTheme

@Composable
fun FavouritePicScreen() {
    PickapicTheme {
        Scaffold(
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = contentColorFor(SnackbarDefaults.backgroundColor)
                .takeOrElse { LocalContentColor.current }
        ) {


            Column(
                modifier = Modifier
            ) {
                TitleCard(text = stringResource(id = R.string.fav_title), Pencil700)
            }
        }
    }
}