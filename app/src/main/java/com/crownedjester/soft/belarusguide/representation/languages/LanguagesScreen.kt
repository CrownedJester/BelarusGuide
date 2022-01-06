package com.crownedjester.soft.belarusguide.representation.languages

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LanguagesScreen(viewModel: LanguagesViewModel = hiltViewModel()) {

    val languagesState = viewModel.languagesState.value

    if (languagesState.error?.isNotBlank()!!) {
        Log.e("LanguagesScreen", languagesState.error)
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                itemsIndexed(languagesState.data!!) { index, language ->
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        text = language.name,
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.body2
                    )

                    if (index + 1 != languagesState.data.size) {
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}