package com.crownedjester.soft.belarusguide.representation.util

import android.location.Geocoder
import java.io.IOException

object GeoUtil {

    fun getAddressByLatLng(geocoder: Geocoder, lat: Double, lng: Double): String =
        try {
            val results = geocoder.getFromLocation(lat, lng, 1)

            results[0].getAddressLine(0)
        } catch (e: IOException) {
            "No address or coordinates"
        }

}
