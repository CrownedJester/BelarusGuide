package com.crownedjester.soft.belarusguide.representation.cities

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.crownedjester.soft.belarusguide.representation.CitiesPlacesViewModel
import com.crownedjester.soft.belarusguide.representation.cities.components.CityItem
import com.crownedjester.soft.belarusguide.representation.util.Screen

@Composable
fun CitiesScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {

    val viewModel: CitiesPlacesViewModel = viewModel(LocalContext.current as ComponentActivity)
    val dataState = viewModel.citiesStateFlow.collectAsState().value

    if (dataState.error?.isNotBlank()!!) {
        Log.e("CitiesScreen", dataState.error)
    }

    Column(modifier = modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(dataState.data!!) { city ->
                CityItem(
                    city = city,
                    onClick = {
                        navController.navigate(Screen.PlacesScreen.route + "/${city.id}")
                    })
            }
        }

    }
}