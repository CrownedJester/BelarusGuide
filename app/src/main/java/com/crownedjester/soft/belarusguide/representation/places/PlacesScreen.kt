package com.crownedjester.soft.belarusguide.representation.places

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.crownedjester.soft.belarusguide.representation.CitiesPlacesViewModel
import com.crownedjester.soft.belarusguide.representation.places.components.PlaceItem
import com.crownedjester.soft.belarusguide.representation.util.Screen

@Composable
fun PlacesScreen(
    modifier: Modifier = Modifier,
    cityId: Int,
    navController: NavController
) {

    val viewModel: CitiesPlacesViewModel = viewModel(LocalContext.current as ComponentActivity)
    val placesState = viewModel.placesStateFlow.collectAsState().value

    if (placesState.error?.isNotBlank()!!) {
        Log.e("PlacesScreen", "Data is empty")

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
                            navController.apply {
                                currentBackStackEntry?.arguments?.putParcelable(
                                    "placeInfo", placeInfo
                                )
                                navigate(Screen.PlaceDetailScreen.route)
                            }
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