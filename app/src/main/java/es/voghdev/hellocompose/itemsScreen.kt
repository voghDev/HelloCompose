package es.voghdev.hellocompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter

@Composable
fun ItemsScreen() {
    Column {
        SampleRow("This is row 1")
        SampleRow("This is row 2")
        SampleRow("This is row 3")
        SampleRow("This is row 4")
    }
}

@Composable
fun SampleRow(text: String) {
    Box {
        HighlightedBackground()
        Column(Modifier.clickable(onClick = { })) {
            SmallSpacer()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = 8.dp,
                        start = 16.dp,
                        end = 16.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SampleImage()
                SmallSpacer()
                RowBody(text = text)
            }
            Separator()
        }
    }
}

@Composable
fun SmallSpacer() =
    Spacer(modifier = Modifier.size(8.dp))

@Composable
private fun SampleImage() = Box(Modifier.size(48.dp)) {
    Image(
        painter = rememberImagePainter(data = "https://lorempixel.com/48/48/people/1/"),
        contentDescription = null,
        modifier = Modifier.padding(start = 16.dp, top = 16.dp)
    )
}

@Composable
fun RowBody(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Black
) {
    Text(
        text = text,
        style = MaterialTheme.typography.body2,
        color = color,
        modifier = modifier,
        lineHeight = 22.sp,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun Separator() {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(
                start = 8.dp,
                end = 8.dp
            )
            .height(1.dp)
            .background(MaterialTheme.colors.onPrimary)
    )
}

@Composable
private fun HighlightedBackground() {
    Card(
        backgroundColor = Color.LightGray,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(start = 12.dp, end = 12.dp)
    ) {}
}