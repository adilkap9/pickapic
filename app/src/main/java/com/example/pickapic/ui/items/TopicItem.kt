package com.example.pickapic.ui.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pickapic.ui.theme.Shapes
import com.example.pickapic.util.TopicModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TopicItem(item: TopicModel, navController: NavController) {

    Card(
        shape = Shapes.large,
        modifier = Modifier.padding(16.dp),
        elevation = 12.dp,
        onClick = {navController.navigate("picScreen/${item.topicName}")}
    ) {
        Image(
            painter = painterResource(id = item.imageId), contentDescription = "image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(240.dp)
                .clip(Shapes.large)
                .padding(0.dp)
        )
        Text(
            text = item.topicName,
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.End,
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 32.dp
            )
        )
    }

}