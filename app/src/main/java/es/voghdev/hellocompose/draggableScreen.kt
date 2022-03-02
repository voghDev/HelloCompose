package es.voghdev.hellocompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter

@Composable
fun DraggableScreen() {
    val headerColors = listOf(Color.Blue, Color.Green, Color.Cyan)
    val bodyColors = listOf(Color.Red, Color.DarkGray, Color.Magenta)
    var currentPage by remember { mutableStateOf(0) }

    Box(
        Modifier
            .fillMaxSize()
    ) {
        Column {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(headerColors[currentPage])
            ) {
                Image(
                    painter = rememberImagePainter("https://picsum.photos/400/200"),
                    contentDescription = "Header image",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                )
            }
            Box(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(bodyColors[currentPage])
            )
        }
    }
}
