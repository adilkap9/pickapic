package com.example.pickapic.ui.screens

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import coil.compose.AsyncImage
import com.example.pickapic.ui.theme.PickapicTheme

@Composable
fun FullPicScreen(
    pictureUrl: String
) {

    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    PickapicTheme {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = pictureUrl,
                contentDescription = "Image",
                modifier = Modifier
                    .align(Alignment.Center)
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = if (scale > 1f) offset.x else 0f,
                        translationY = if (scale > 1f) offset.y else 0f
                    )
                    .pointerInput(Unit) {
                        detectTransformGestures(
                            onGesture = { _, pan: Offset, zoom: Float, _ ->
                                offset += pan
                                scale = (scale * zoom).coerceIn(1.5f, 4f)
                            }
                        )
                    }
            )
        }
    }


}