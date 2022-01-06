package com.crownedjester.soft.belarusguide.representation.cities.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.crownedjester.soft.belarusguide.data.model.CityDto

@Composable
fun CityItem(
    modifier: Modifier = Modifier,
    city: CityDto,
    onClick: () -> Unit,
    navController: NavController? = null
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val painter = rememberImagePainter(data = city.logo)
            Image(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(64.dp)
                    .padding(start = 12.dp, bottom = 4.dp, top = 4.dp, end = 12.dp),
                painter = painter,
                contentDescription = "town logo"
            )

            Text(
                modifier = Modifier
                    .padding(start = 12.dp, top = 12.dp)
                    .align(Alignment.Top),
                text = buildAnnotatedString {
                    withStyle(SpanStyle(fontStyle = FontStyle.Normal, fontSize = 16.sp)) {
                        append(city.name + "(")
                    }
                    withStyle(SpanStyle(fontStyle = FontStyle.Italic, fontSize = 12.sp)) {
                        append(city.region)
                    }
                    withStyle(SpanStyle(fontStyle = FontStyle.Normal, fontSize = 16.sp)) {
                        append(")")
                    }
                }
            )
        }
    }
}