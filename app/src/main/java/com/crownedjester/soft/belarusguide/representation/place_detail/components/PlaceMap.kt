package com.crownedjester.soft.belarusguide.representation.place_detail.components

import android.location.Geocoder
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.crownedjester.soft.belarusguide.representation.util.GeoUtil
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun PlaceMap(modifier: Modifier = Modifier, lat: Double, lng: Double, geocoder: Geocoder) {
    val mapView = rememberMapViewWithLifecycle()
    AndroidView(
        modifier = modifier
            .padding(start = 6.dp, end = 6.dp, top = 12.dp)
            .fillMaxWidth()
            .height(360.dp),
        factory = { mapView }) { composeMapView ->
        CoroutineScope(Dispatchers.Main).launch {
            composeMapView.map.apply {
                isZoomGesturesEnabled = true
                val point = Point(lat, lng)
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
        text = GeoUtil.getAddressByLatLng(geocoder, lat, lng),
        textAlign = TextAlign.End,
        fontStyle = FontStyle.Italic,
        style = MaterialTheme.typography.body2
    )
}