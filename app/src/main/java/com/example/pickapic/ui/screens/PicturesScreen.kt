package com.example.pickapic.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.pickapic.R
import com.example.pickapic.ui.composables.TitleCard
import com.example.pickapic.ui.items.PictureItem
import com.example.pickapic.ui.items.PictureUiModel
import com.example.pickapic.ui.theme.PickapicTheme
import com.example.pickapic.ui.viewmodels.PicturesViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

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
                TitleCard(text = if (topic.length < 14) topic else "Search")
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PicturesLoadedScreen(pictures: PictureUiModel, navController: NavController) {

    val scrollState = rememberLazyListState()
    val staggeredGridState = rememberLazyStaggeredGridState()

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
                      PictureItem(
                          pictureUrl = pictures.results[index].urls.regular,
                          onClick = {
                              val encodedUrl = URLEncoder.encode(
                                  pictures.results[index].urls.regular,
                                  StandardCharsets.UTF_8.toString()
                              )
                              navController.navigate("fullPicScreen/$encodedUrl")
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