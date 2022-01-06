package com.crownedjester.soft.belarusguide.representation.util

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Language
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.crownedjester.soft.belarusguide.common.Constants

@Composable
fun TopBar(navController: NavController) {

    TopAppBar(
        title = {
            Text(text = Constants.TOP_BAR_TITLE, style = MaterialTheme.typography.h6)
        },
        navigationIcon = {
            IconButton(content = {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "nav back icon"
                )
            }, onClick = {
                navController.previousBackStackEntry?.id?.let { prevRouteId ->
                    navController.navigate(
                        prevRouteId
                    )
                }
            })
        },
        actions = {
            IconButton(
                content = {
                    Icon(
                        imageVector = Icons.Filled.Language,
                        contentDescription = "language screen"
                    )
                }, onClick = {
                    navController.navigate(Screen.LanguagesScreen.route)
                }
            )
        }
    )
}