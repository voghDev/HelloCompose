package es.voghdev.hellocompose

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter

@ExperimentalAnimationApi
@Composable
fun ItemsScreen() {
    Column {
        SampleRow("This is row 1", 100)
        SampleRow("This is row 2", 0)
        SampleRow("This is row 3", 133, likedByMe = true)
        SampleRow("This is row 4", 40)
    }
}

@ExperimentalAnimationApi
@Composable
fun SampleRow(
    text: String,
    count: Int = 0,
    likedByMe: Boolean = false
) {
    var updatedCount by remember { mutableStateOf(count) }
    var isLiked by remember { mutableStateOf(likedByMe) }
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
                RowTitle(modifier = Modifier.weight(1f), text = text)
                LikeCount(updatedCount)
                MinSpacer()
                LikeIcon(isLiked) { updatedLike ->
                    if (updatedLike)
                        updatedCount++
                    else
                        updatedCount--

                    isLiked = updatedLike
                }
                SmallSpacer()
            }
            Separator()
        }
    }
}

@Composable
private fun SmallSpacer() =
    Spacer(modifier = Modifier.size(8.dp))

@Composable
private fun MinSpacer() =
    Spacer(modifier = Modifier.size(4.dp))

@Composable
private fun SampleImage() = Box(Modifier.size(48.dp)) {
    Image(
        painter = rememberImagePainter(data = "https://lorempixel.com/48/48/people/1/"),
        contentDescription = null,
        modifier = Modifier.padding(start = 16.dp, top = 16.dp)
    )
}

@Composable
private fun RowTitle(
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

@ExperimentalAnimationApi
@Composable
fun LikeCount(updatedCount: Int) {
    AnimatedContent(
        targetState = updatedCount,
        transitionSpec = {
            slideInVertically { height -> height } with
                slideOutVertically { height -> -height }
        }
    ) {
        Text(
            text = "$it",
            style = MaterialTheme.typography.body2,
            color = Color.Black,
            lineHeight = 22.sp,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun LikeIcon(
    isLiked: Boolean,
    onLikeClicked: (Boolean) -> Unit
) {
    Crossfade(
        targetState = isLiked,
        animationSpec = tween(durationMillis = 800, easing = LinearOutSlowInEasing)
    ) { animatedValue ->
        val source:ImageVector
        val tint: Color
        if (animatedValue) {
            source = Icons.Default.ThumbUp
            tint = Color.Blue
        } else {
            source = Icons.Outlined.ThumbUp
            tint = Color.Black
        }
        Icon(
            imageVector = source,
            contentDescription = "Like icon",
            tint = tint,
            modifier = Modifier.clickable { onLikeClicked(!isLiked) }
        )
    }
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
