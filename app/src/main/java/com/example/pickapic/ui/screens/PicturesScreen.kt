package com.example.pickapic.ui.screens

import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.pickapic.R
import com.example.pickapic.ui.composables.TitleCard
import com.example.pickapic.ui.items.PictureItem
import com.example.pickapic.ui.items.PictureUiModel
import com.example.pickapic.ui.theme.Pencil700
import com.example.pickapic.ui.theme.PickapicTheme
import com.example.pickapic.ui.theme.Shapes
import com.example.pickapic.ui.viewmodels.PicturesViewModel
import kotlinx.coroutines.delay

@Composable
fun PicturesScreen(
    topic: String,
    viewModel: PicturesViewModel = hiltViewModel(),
    navController: NavController
) {

    viewModel.fetchPicturesByTopic(topic = topic)

    PickapicTheme {
        Scaffold(
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = contentColorFor(SnackbarDefaults.backgroundColor)
                .takeOrElse { LocalContentColor.current }
        ) {

            Column(
                modifier = Modifier
            ) {
                TitleCard(text = if (topic.length < 14) topic else "Search", Pencil700)
                when (val state = viewModel.uiState.collectAsState().value) {
                    is PicturesViewModel.PicturesUiState.Empty -> Text(
                        text = stringResource(id = R.string.no_data_available),
                        modifier = Modifier.padding(16.dp)
                    )
                    is PicturesViewModel.PicturesUiState.Loading -> Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                    is PicturesViewModel.PicturesUiState.Error -> ErrorDialog(state.message)
                    is PicturesViewModel.PicturesUiState.Loaded -> PicturesLoadedScreen(
                        pictures = state.data,
                        navController = navController
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
private fun PicturesLoadedScreen(pictures: PictureUiModel, viewModel: PicturesViewModel = hiltViewModel(), navController: NavController) {

    val scrollState = rememberLazyListState()
    val staggeredGridState = rememberLazyStaggeredGridState()

    var dialogOpen by remember {
        mutableStateOf(false)
    }

    if (dialogOpen) {
        var isLiked by remember {
            mutableStateOf(false)
        }

        Dialog(
            onDismissRequest = { dialogOpen = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                modifier = Modifier
                    .wrapContentSize(),
                shape = Shapes.large
            ) {
                Column(
                    modifier = Modifier.wrapContentSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier.wrapContentSize()
                    ) {
                        var scale by remember { mutableStateOf(1f) }
                        var offset by remember { mutableStateOf(Offset.Zero) }
                        val picUrl = viewModel.currentPictureUrl.collectAsState().value
                        AsyncImage(
                            model = picUrl,
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
//                                    detectTransformGestures(
//                                        onGesture = { _, pan: Offset, zoom: Float, _ ->
//                                            offset += pan
//                                            scale = (scale * zoom).coerceIn(1.5f, 4f)
//                                        }
//                                    )
                                    detectTapGestures(
                                        onDoubleTap = {
                                            Log.d("Debuger", "Liked")
                                            isLiked = true
                                        }
                                    )
                                }
                        )
                        if (isLiked) {
                            AnimatedHeartIcon(modifier = Modifier
                                .align(Alignment.Center)
                                .size(64.dp),
                                heartSize = 64.dp,
                                heartColor = Color.White
                            )
                            LaunchedEffect(Unit) {
                                delay(800)
                                isLiked = false
                            }
                        }
                    }
                }
            }
        }
    }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(
            start = 2.dp,
            top = 2.dp,
            end = 2.dp,
            bottom = 2.dp
        ),
        content = {
                  items(pictures.results.size) { index ->
                      val pictureUrl = pictures.results[index].urls.regular
                      PictureItem(
                          pictureUrl = pictureUrl,
                          onClick = {
                              viewModel.updateCurrentPictureUrl(pictureUrl)
                              dialogOpen = true
                          }
                      )
                  }
        },
        state = staggeredGridState
    )
    LaunchedEffect(key1 = remember { derivedStateOf { scrollState.layoutInfo } }.value.totalItemsCount, block = {
        staggeredGridState.animateScrollToItem(scrollState.firstVisibleItemIndex)
    })
}

@Composable
fun ErrorDialog(message: String) {
    val openDialog = remember { mutableStateOf(true) }
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = stringResource(R.string.problem_occurred))
            },
            text = {
                Text(message)
            },
            confirmButton = {
                openDialog.value = false
            }
        )
    }
}

@Composable
fun AnimatedHeartIcon(
    modifier: Modifier = Modifier,
    heartColor: Color = Color.Red,
    heartSize: Dp = 128.dp
) {
    val infiniteTransition = rememberInfiniteTransition()
    val size by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 400),
            repeatMode = RepeatMode.Reverse
        )
    )
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 400),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = modifier.size(heartSize * size),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "Favorite",
            tint = heartColor.copy(alpha = alpha),
            modifier = Modifier.size(heartSize * size)
        )
    }
}


