package com.crownedjester.soft.belarusguide.representation.place_detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import com.crownedjester.soft.belarusguide.data.model.PlaceInfo

@Composable
fun PlaceDetailScreen(placeInfo: PlaceInfo) {

    var isPlaying by remember { mutableStateOf(false) }
    var playingProgress by remember { mutableStateOf(0f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        Text(
            text = placeInfo.name,
            Modifier.padding(4.dp),
            style = MaterialTheme.typography.h5,
            textAlign = TextAlign.Center
        )

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .padding(4.dp)
        )

        val painter = rememberImagePainter(placeInfo.photo, builder = {
            transformations(RoundedCornersTransformation(6f))
        })
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp),
            painter = painter,
            contentDescription = "place detail photo"
        )

        if (placeInfo.sound.isBlank()) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                style = MaterialTheme.typography.body2,
                fontStyle = FontStyle.Italic,
                text = "No such audio"
            )
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(4.dp),
                border = BorderStroke(2.dp, Color.Gray),
                shape = RoundedCornerShape(16.dp),
                backgroundColor = Color.LightGray
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {

                    IconToggleButton(
                        onCheckedChange = {
                            isPlaying = !isPlaying
                        },
                        modifier = Modifier
                            .size(36.dp)
                            .align(Alignment.CenterVertically),
                        checked = isPlaying
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Filled.Stop else Icons.Filled.PlayArrow,
                            contentDescription = "play button"
                        )
                    }

                    LinearProgressIndicator(
                        progress = playingProgress,
                        modifier = Modifier
                            .fillMaxHeight()
                            .height(4.dp)
                            .align(Alignment.CenterVertically)
                    )
                }
            }

        }
        Text(
            text = "\t\t${placeInfo.text}",
            textAlign = TextAlign.Justify,
            style = MaterialTheme.typography.body2
        )

        //MapView
    }
}