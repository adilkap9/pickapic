package com.example.pickapic.ui.items

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.pickapic.ui.theme.Shapes
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PictureItem(pictureUrl: String, onClick: () -> Unit) {

    Box {
        Card(
            shape = Shapes.large,
            modifier = Modifier.padding(3.dp),
            elevation = 12.dp,
            onClick = onClick
        ) {
            AsyncImage(
                model = pictureUrl,
                contentDescription = null
            )
        }
    }
}