package com.crownedjester.soft.belarusguide.representation.place_detail

import android.location.Geocoder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.crownedjester.soft.belarusguide.representation.common_components.ErrorScreen
import com.crownedjester.soft.belarusguide.representation.common_components.LoadingCircleProgress
import com.crownedjester.soft.belarusguide.representation.place_detail.components.PlaceMap
import com.crownedjester.soft.belarusguide.representation.place_detail.components.PlayerProgress
import com.crownedjester.soft.belarusguide.representation.places.PlacesViewModel
import com.crownedjester.soft.belarusguide.representation.util.DateUtil
import com.crownedjester.soft.belarusguide.representation.util.StringUtil.formatPlaceDescription
import java.util.*

@Composable
fun PlaceDetailScreen(
    modifier: Modifier = Modifier,
    playerViewModel: PlayerViewModel = hiltViewModel(),
    placeId: Int,
    currentLangId: Int
) {

    val context = LocalContext.current

    val geocoder = Geocoder(context, Locale.getDefault())

    val remainingSeconds by playerViewModel.remainingSeconds.collectAsState()

    val sharedViewModel =
        viewModel<PlacesViewModel>(LocalContext.current as ComponentActivity)

    val placesState = sharedViewModel.placesStateFlow.collectAsState().value

    val progressValue by animateFloatAsState(
        targetValue = (playerViewModel.getDurationSeconds() - remainingSeconds.toFloat()) / playerViewModel.getDurationSeconds()
    )

    DisposableEffect(key1 = true) {
        onDispose {
            playerViewModel.onEvent(PlayerEvent.OnRelease)
            Log.i("PlaceDetailScreen", "Player has released")
        }
    }

    if (placesState.error?.isNotBlank() == true) {
        ErrorScreen(
            message = placesState.error,
            onRetry = { sharedViewModel.retryRetrievePlaces() })
    } else if (placesState.isLoading) {
        LoadingCircleProgress()
    } else {

        val (_, _, name, text, sound, _, lat, lng, _, photo, _, _, lastEditTime) = placesState.data!!.first { it.id == placeId && it.lang == currentLangId }

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(4.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = name,
                Modifier.padding(4.dp),
                style = MaterialTheme.typography.h5,
                textAlign = TextAlign.Center
            )

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 12.dp, top = 4.dp),
                text = "Edited: ${DateUtil.convertTimestampToDate(lastEditTime)}",
                fontStyle = FontStyle.Italic,
                style = MaterialTheme.typography.body2,
                textAlign = TextAlign.End
            )

            val painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current).data(photo).apply {
                    transformations(RoundedCornersTransformation(6f))
                }.build()
            )
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                painter = painter,
                contentDescription = "place detail photo"
            )

            PlayerProgress(
                sound = sound,
                progress = progressValue,
                playerText = playerViewModel.toStringFormat(),
                onStart = { playerViewModel.onEvent(PlayerEvent.OnStart) }
            ) { playerViewModel.onEvent(PlayerEvent.OnPause) }


            Text(
                text = "\t\t${text.formatPlaceDescription()}",
                textAlign = TextAlign.Justify,
                style = MaterialTheme.typography.body2
            )

            PlaceMap(lat = lat, lng = lng, geocoder = geocoder)

        }
    }
}