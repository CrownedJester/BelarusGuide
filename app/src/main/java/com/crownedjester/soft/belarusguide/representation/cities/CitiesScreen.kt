package com.crownedjester.soft.belarusguide.representation.cities

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.crownedjester.soft.belarusguide.representation.cities.components.CityItem

@Composable
fun CitiesScreen(
    modifier: Modifier = Modifier,
    viewModel: CitiesViewModel = hiltViewModel()
) {

    val dataState = viewModel.dataState.value

    if (dataState.error?.isNotBlank()!!) {
        Log.e("CitiesScreen", dataState.error)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(dataState.data!!) { city ->
                CityItem(city = city, onClick = { /*TODO*/ })

            }

        }

    }
}