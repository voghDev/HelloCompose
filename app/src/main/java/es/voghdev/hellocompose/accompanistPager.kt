package es.voghdev.hellocompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AccompanistPagerScreen() {
    val headerColors =
        listOf(Color.Red, Color.Blue, Color.Magenta)
    val bodyColors =
        listOf(Color.Gray, Color.DarkGray, Color.LightGray)
    val pagerState = rememberPagerState()

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        HorizontalPager(
            count = headerColors.size,
            state = pagerState
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
                ) {
                    key(currentPage) {
                        Text("This is the page $currentPage")
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        pagerState.scrollToPage(1)
    }
}