package com.crownedjester.soft.belarusguide.representation.util

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.crownedjester.soft.belarusguide.common.Constants

@Composable
fun TopBar(
    navController: NavController,
    onClick: () -> Unit,
    isDarkMode: Boolean
) {

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
                navController.enableOnBackPressed(true)
                if (navController.currentDestination?.route != navController.graph.startDestinationRoute) {
                    navController.navigateUp()
                }
            })
        },
        actions = {
            IconButton(
                onClick = onClick,
                content = {
                    Icon(
                        imageVector = if (isDarkMode) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                        contentDescription = "theme"
                    )
                })

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