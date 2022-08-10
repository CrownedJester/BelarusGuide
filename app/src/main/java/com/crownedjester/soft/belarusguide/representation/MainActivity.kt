package com.crownedjester.soft.belarusguide.representation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.crownedjester.soft.belarusguide.representation.cities.CitiesScreen
import com.crownedjester.soft.belarusguide.representation.languages.LanguagesScreen
import com.crownedjester.soft.belarusguide.representation.place_detail.PlaceDetailScreen
import com.crownedjester.soft.belarusguide.representation.places.PlacesScreen
import com.crownedjester.soft.belarusguide.representation.ui.theme.BelarusGuideTheme
import com.crownedjester.soft.belarusguide.representation.util.BundleUtil.CITY_ID_KEY
import com.crownedjester.soft.belarusguide.representation.util.BundleUtil.PLACE_ID_KEY
import com.crownedjester.soft.belarusguide.representation.util.BundleUtil.SOUND_KEY
import com.crownedjester.soft.belarusguide.representation.util.Screen
import com.crownedjester.soft.belarusguide.representation.util.TopBar
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val sharedViewModel: CitiesPlacesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MapKitFactory.initialize(this)

        setContent {

            val viewModel: ThemeViewModel = hiltViewModel()
            val isDarkMode by viewModel.isDarkMode.collectAsState()

            BelarusGuideTheme(isDarkMode) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    val navController = rememberNavController()
                    val scaffoldState = rememberScaffoldState()

                    Scaffold(
                        scaffoldState = scaffoldState,
                        topBar = {
                            TopBar(
                                navController = navController,
                                onChangeTheme = { viewModel.changeTheme() },
                                isDarkMode = isDarkMode
                            )
                        }
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
                                Screen.PlacesScreen.route + "/{$CITY_ID_KEY}",
                                arguments = listOf(
                                    navArgument(CITY_ID_KEY) {
                                        type = NavType.IntType
                                    },
                                )
                            ) {
                                it.arguments?.getInt(CITY_ID_KEY)?.let { cityId ->
                                    PlacesScreen(
                                        navController = navController,
                                        cityId = cityId
                                    )
                                }
                            }

                            composable(Screen.PlaceDetailScreen.route + "?placeId={$PLACE_ID_KEY}&sound={$SOUND_KEY}",
                                arguments = listOf(
                                    navArgument(PLACE_ID_KEY) {
                                        type = NavType.IntType
                                    },
                                    navArgument(SOUND_KEY) {
                                        type = NavType.StringType
                                    }
                                )) {
                                it.arguments?.getInt(PLACE_ID_KEY)?.let { placeId ->
                                    PlaceDetailScreen(
                                        placeId = placeId
                                    )
                                }
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

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        super.onStop()
        MapKitFactory.getInstance().onStop()
    }
}