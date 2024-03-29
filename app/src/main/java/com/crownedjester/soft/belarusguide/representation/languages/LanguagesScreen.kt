package com.crownedjester.soft.belarusguide.representation.languages

import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.crownedjester.soft.belarusguide.representation.common_components.ErrorScreen
import com.crownedjester.soft.belarusguide.representation.common_components.LoadingCircleProgress
import com.crownedjester.soft.belarusguide.representation.util.Screen
import kotlinx.coroutines.FlowPreview

@OptIn(FlowPreview::class)
@Composable
fun LanguagesScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {

    val viewModel: LanguagesViewModel = viewModel(LocalContext.current as ComponentActivity)
    val languagesState = viewModel.languagesState.value

    if (languagesState.error?.isNotBlank()!!) {
        ErrorScreen(message = languagesState.error, onRetry = { viewModel.retryRetrieveLanguages() })
    } else if (languagesState.isLoading) {
        LoadingCircleProgress()
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(4.dp)
        ) {
            Text(text = "Choose content language: ", style = MaterialTheme.typography.h6)
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                itemsIndexed(languagesState.data!!) { _, language ->
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(36.dp)
                            .padding(start = 16.dp, bottom = 8.dp, top = 8.dp)
                            .clickable {
                                viewModel.setDataLanguage(language.id)
                                if (navController.previousBackStackEntry?.destination?.route?.contains(
                                        Screen.PlaceDetailScreen.route
                                    ) == true
                                ) {
                                    navController.popBackStack()
                                    navController.navigateUp()
                                } else {
                                    navController.navigateUp()
                                }

                            },
                        text = language.name,
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.body1
                    )

                    Divider(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .height(2.dp)
                            .padding(horizontal = 1.dp),
                        color = Color.Gray
                    )
                }
            }
        }
    }
}