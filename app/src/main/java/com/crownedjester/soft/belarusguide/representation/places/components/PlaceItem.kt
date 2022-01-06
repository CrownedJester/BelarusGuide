package com.crownedjester.soft.belarusguide.representation.places.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import com.crownedjester.soft.belarusguide.data.model.PlaceInfo

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlaceItem(
    modifier: Modifier = Modifier, onClick: () -> Unit, placeInfo: PlaceInfo
) {

    Card(
        modifier = modifier,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val painter = rememberImagePainter(placeInfo.photo, builder = {
                transformations(RoundedCornersTransformation(6f))
            })
            Image(
                modifier = Modifier
                    .height(240.dp)
                    .fillMaxWidth(),
                painter = painter,
                contentDescription = "place logo"
            )

            Text(
                text = placeInfo.name,
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center
            )

        }
    }
}