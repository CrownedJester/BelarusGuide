package com.crownedjester.soft.belarusguide.representation.places

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.crownedjester.soft.belarusguide.representation.CitiesPlacesViewModel
import com.crownedjester.soft.belarusguide.representation.common_components.ErrorScreen
import com.crownedjester.soft.belarusguide.representation.common_components.LoadingCircleProgress
import com.crownedjester.soft.belarusguide.representation.places.components.PlaceItem
import com.crownedjester.soft.belarusguide.representation.util.Screen
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay

@OptIn(FlowPreview::class)
@Composable
fun PlacesScreen(
    modifier: Modifier = Modifier, navController: NavController, cityId: Int
) {

    val viewModel: CitiesPlacesViewModel = viewModel(LocalContext.current as ComponentActivity)
    val placesState = viewModel.placesStateFlow.collectAsState().value

    if (placesState.error?.isNotBlank() == true) {
        ErrorScreen(
            message = placesState.error, onRetry = { viewModel.retryRetrieveSharedData() }
        )
    } else if (placesState.isLoading) {
        LoadingCircleProgress()
        LaunchedEffect(key1 = placesState, block = { delay(1500) })
    } else {
        Box(
            modifier = modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
            ) {
                itemsIndexed(placesState.data!!.filter { it.cityId == cityId }) { i, placeInfo ->
                    PlaceItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(320.dp), onClick = {
                            navController.navigate(
                                Screen.PlaceDetailScreen.route +
                                        "?placeId=${placeInfo.id}&sound=${placeInfo.sound}"
                            )
                        }, placeInfo = placeInfo
                    )

                    if (i + 1 != placesState.data.size) {
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.5.dp)
                                .padding(horizontal = 5.dp), color = Color.Gray
                        )
                    }

                }
            }
        }
    }
}