package com.crownedjester.soft.belarusguide

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BelarusGuideApp : Application() {

    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(this.getString(R.string.api_key))

    }
}