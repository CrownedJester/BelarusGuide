package com.crownedjester.soft.belarusguide.representation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.crownedjester.soft.belarusguide.data.model.PlaceInfo
import com.crownedjester.soft.belarusguide.representation.cities.CitiesScreen
import com.crownedjester.soft.belarusguide.representation.languages.LanguagesScreen
import com.crownedjester.soft.belarusguide.representation.place_detail.PlaceDetailScreen
import com.crownedjester.soft.belarusguide.representation.places.PlacesScreen
import com.crownedjester.soft.belarusguide.representation.ui.theme.BelarusGuideTheme
import com.crownedjester.soft.belarusguide.representation.util.Screen
import com.crownedjester.soft.belarusguide.representation.util.TopBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BelarusGuideTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    val scaffoldState = rememberScaffoldState()

                    Scaffold(
                        scaffoldState = scaffoldState,
                        topBar = { TopBar(navController = navController) }
                    ) { paddingValues ->

                        NavHost(
                            modifier = Modifier.padding(paddingValues),
                            startDestination = Screen.CitiesScreen.route,
                            navController = navController
                        ) {
                            composable(Screen.CitiesScreen.route) {
                                CitiesScreen(navController = navController)
                            }

                            composable(
                                Screen.PlacesScreen.route + "/{cityId}", arguments = listOf(
                                    navArgument("cityId") {
                                        type = NavType.IntType
                                    })
                            ) {
                                PlacesScreen(navController = navController)
                            }

                            composable(Screen.PlaceDetailScreen.route) {
                                val placeInfo =
                                    navController.previousBackStackEntry?.arguments?.getParcelable<PlaceInfo>(
                                        "placeInfo"
                                    )
                                placeInfo?.let { place -> PlaceDetailScreen(placeInfo = place) }
                            }
                            composable(Screen.LanguagesScreen.route) {
                                LanguagesScreen(navController = navController)
                            }
                        }
                    }
                }
            }
        }
    }
}