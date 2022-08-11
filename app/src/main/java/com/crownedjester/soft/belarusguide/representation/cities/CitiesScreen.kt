package com.crownedjester.soft.belarusguide.representation.cities

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
import com.crownedjester.soft.belarusguide.representation.common_components.ErrorScreen
import com.crownedjester.soft.belarusguide.representation.common_components.LoadingCircleProgress
import com.crownedjester.soft.belarusguide.representation.util.Screen
import kotlinx.coroutines.FlowPreview

@OptIn(FlowPreview::class)
@Composable
fun CitiesScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {

    val viewModel: CitiesPlacesViewModel = viewModel(LocalContext.current as ComponentActivity)
    val citiesState = viewModel.citiesStateFlow.collectAsState().value

    if (citiesState.error?.isNotBlank() == true) {
        ErrorScreen(message = citiesState.error, onRetry = { viewModel.retryRetrieveSharedData() })
    } else if (citiesState.isLoading) {
        LoadingCircleProgress()
    } else {
        Column(modifier = modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(citiesState.data!!) { city ->
                    CityItem(
                        city = city,
                        onClick = {
                            navController.navigate(Screen.PlacesScreen.route + "/${city.id}")
                        })
                }
            }

        }
    }
}