package com.crownedjester.soft.belarusguide.representation.place_detail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp


@Composable
fun PlayerProgress(
    modifier: Modifier = Modifier,
    sound: String,
    progress: Float,
    playerText: String,
    onStart: () -> Unit,
    onPause: () -> Unit
) {
    var isPlaying by remember { mutableStateOf(false) }

    if (sound.isBlank()) {
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
            modifier = modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(4.dp),
            border = BorderStroke(2.dp, Color.Gray),
            shape = RoundedCornerShape(16.dp),
            backgroundColor = Color.LightGray
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = {
                        isPlaying = !isPlaying
                        if (isPlaying) {
                            onStart()
                        } else {
                            onPause()
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.CenterVertically),
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        contentDescription = "play button",
                        tint = Color.Black
                    )
                }

                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .height(10.dp)
                        .align(Alignment.CenterVertically),
                    color = Color(0xFFFFD54F),
                    backgroundColor = Color(0xFFC5CAE9)
                )

                Text(text = playerText, color = Color.Black)

            }
        }

    }
}