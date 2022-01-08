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
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import com.crownedjester.soft.belarusguide.data.model.PlaceInfo
import com.crownedjester.soft.belarusguide.representation.place_detail.components.rememberMapViewWithLifecycle
import com.crownedjester.soft.belarusguide.representation.util.GeoUtil
import com.crownedjester.soft.belarusguide.representation.util.StringUtil.formatPlaceDescription
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun PlaceDetailScreen(placeInfo: PlaceInfo) {

    var isPlaying by remember { mutableStateOf(false) }
    var playingProgress by remember { mutableStateOf(0f) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
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

        val painter = rememberImagePainter(placeInfo.photo, builder = {
            transformations(RoundedCornersTransformation(6f))
        })
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
                        prepareAsync()
                    }


//                    playingProgress = 1f / mediaPlayer.duration
                    IconToggleButton(
                        onCheckedChange = {
                            isPlaying = it
                            if (it) {
                                mediaPlayer.start()
                            } else {
                                mediaPlayer.pause()
                            }
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.CenterVertically),
                        checked = isPlaying
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                            contentDescription = "play button"
                        )
                    }

                    LinearProgressIndicator(
                        progress = playingProgress,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .padding(end = 16.dp)
                            .align(Alignment.CenterVertically)
                    )
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
                .padding(8.dp)
                .fillMaxWidth()
                .height(360.dp),
            factory = { mapView }) { composeMapView ->
            CoroutineScope(Dispatchers.Main).launch {
                composeMapView.getMapAsync { map ->
                    map.uiSettings.isZoomControlsEnabled = true

                    val pointPosition = LatLng(placeInfo.lat, placeInfo.lng)
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(pointPosition, 15f))

                    val marker = MarkerOptions().position(pointPosition)
                    map.addMarker(marker)
                }
            }
        }

        val geocoder = Geocoder(context, Locale.getDefault())
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = GeoUtil.getAddressByLatLng(geocoder, placeInfo.lat, placeInfo.lng),
            textAlign = TextAlign.End,
            fontStyle = FontStyle.Italic,
            style = MaterialTheme.typography.body2
        )
    }
}