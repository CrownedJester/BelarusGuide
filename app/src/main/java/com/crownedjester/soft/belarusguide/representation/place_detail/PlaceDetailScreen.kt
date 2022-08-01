package com.crownedjester.soft.belarusguide.representation.place_detail

import android.location.Geocoder
import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.crownedjester.soft.belarusguide.data.model.PlaceInfo
import com.crownedjester.soft.belarusguide.representation.place_detail.components.rememberMapViewWithLifecycle
import com.crownedjester.soft.belarusguide.representation.util.DateUtil
import com.crownedjester.soft.belarusguide.representation.util.GeoUtil
import com.crownedjester.soft.belarusguide.representation.util.StringUtil.formatPlaceDescription
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun PlaceDetailScreen(
    modifier: Modifier = Modifier,
    placeInfo: PlaceInfo,
    navController: NavController
) {

    var isPlaying by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val geocoder = Geocoder(context, Locale.getDefault())

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(4.dp)
            .verticalScroll(rememberScrollState())
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
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 12.dp, top = 4.dp),
            text = "Edited: ${DateUtil.convertTimestampToDate(placeInfo.lastEditTime)}",
            fontStyle = FontStyle.Italic,
            style = MaterialTheme.typography.body2,
            textAlign = TextAlign.End
        )

        val painter = rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current).data(placeInfo.photo)
                .apply(block = fun ImageRequest.Builder.() {
                    transformations(RoundedCornersTransformation(6f))
                }).build()
        )
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
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
                    .height(64.dp)
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

                    val mediaPlayer = MediaPlayer().apply {
                        setAudioAttributes(
                            AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .build()
                        )
                        setDataSource(placeInfo.sound)
                        prepare()
                    }

                    IconButton(
                        onClick = {
                            isPlaying = !isPlaying
                            if (isPlaying) {
                                mediaPlayer.start()
                            } else {
                                mediaPlayer.pause()
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
                        progress = (mediaPlayer.currentPosition / mediaPlayer.duration).toFloat(),
                        modifier = Modifier
                            .height(16.dp)
                            .align(Alignment.CenterVertically)
                    )

                    navController.addOnDestinationChangedListener { controller, destination, _ ->
                        if (mediaPlayer.isPlaying) {
                            mediaPlayer.stop()
                        }
                    }

                }
            }

        }
        Text(
            text = "\t\t${placeInfo.text.formatPlaceDescription()}",
            textAlign = TextAlign.Justify,
            style = MaterialTheme.typography.body2
        )


        //MapView
        val mapView = rememberMapViewWithLifecycle()
        AndroidView(
            modifier = Modifier
                .padding(start = 6.dp, end = 6.dp, top = 12.dp)
                .fillMaxWidth()
                .height(360.dp),
            factory = { mapView }) { composeMapView ->
            CoroutineScope(Dispatchers.Main).launch {
                composeMapView.map.apply {
                    isZoomGesturesEnabled = true
                    val point = Point(placeInfo.lat, placeInfo.lng)
                    move(
                        CameraPosition(
                            point,
                            14f,
                            0f,
                            0f
                        )
                    )

                    mapObjects.addPlacemark(point)
                }
            }
        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp, end = 8.dp),
            text = GeoUtil.getAddressByLatLng(geocoder, placeInfo.lat, placeInfo.lng),
            textAlign = TextAlign.End,
            fontStyle = FontStyle.Italic,
            style = MaterialTheme.typography.body2
        )

    }

}