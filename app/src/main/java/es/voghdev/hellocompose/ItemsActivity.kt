package es.voghdev.hellocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberImagePainter
import es.voghdev.hellocompose.ui.theme.HelloComposeTheme

data class VideoResultEntity(
    val id: String,
    val preview: String,
    val name: String
)

val videos = listOf(
    VideoResultEntity(
        "001",
        "https://via.placeholder.com/150",
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
    ),
    VideoResultEntity(
        "002",
        "https://via.placeholder.com/150",
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4"
    ),
    VideoResultEntity(
        "003",
        "https://via.placeholder.com/150",
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
    ),
    VideoResultEntity(
        "004",
        "https://via.placeholder.com/150",
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
    ),
    VideoResultEntity(
        "005",
        "https://via.placeholder.com/150",
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
    ),
    VideoResultEntity(
        "006",
        "https://via.placeholder.com/150",
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
    ),
    VideoResultEntity(
        "007",
        "https://via.placeholder.com/150",
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4",
    ),
    VideoResultEntity(
        "008",
        "https://via.placeholder.com/150",
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4",
    ),
    VideoResultEntity(
        "010",
        "https://via.placeholder.com/150",
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackOnStreetAndDirt.mp4",
    ),
    VideoResultEntity(
        "011",
        "https://via.placeholder.com/150",
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
    ),
    VideoResultEntity(
        "012",
        "https://via.placeholder.com/150",
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/VolkswagenGTIReview.mp4",
    ),
    VideoResultEntity(
        "013",
        "https://via.placeholder.com/150",
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4",
    ),
    VideoResultEntity(
        "014",
        "https://via.placeholder.com/150",
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WhatCarCanYouGetForAGrand.mp4",
    )
)

class ItemsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelloComposeTheme {
                Box {
                    VideoPlayList(gameVideos = videos)
                }
            }
        }
    }
}

@Composable
fun VideoPlayList(
    modifier: Modifier = Modifier,
    gameVideos: List<VideoResultEntity>
) {
    val state = rememberLazyListState()

    LazyColumn(modifier = modifier, state = state) {
        itemsIndexed(
            items = gameVideos,
            key = { _, item -> item.id }
        ) { index, item ->
            VideoItem(index = index, video = item)
        }
    }
}

@Composable
fun VideoItem(index: Int, video: VideoResultEntity) {
    val currentlyPlaying = remember { mutableStateOf(true) }

    ConstraintLayout(
        modifier =
        Modifier
            .testTag("VideoParent $index")
            .padding(8.dp)
            .wrapContentSize()
    ) {
        val (thumbnail, play, title, nowPlaying) =
            createRefs()

        // thumbnail
        Image(
            contentScale = ContentScale.Crop,
            painter =
            rememberImagePainter(
                data = video.preview,
                builder = {
                    placeholder(R.drawable.ic_launcher_foreground)
                    crossfade(true)
                }
            ),
            contentDescription = "Thumbnail",
            modifier =
            Modifier
                .height(120.dp)
                .width(120.dp)
                .clip(RoundedCornerShape(20.dp))
                .shadow(elevation = 20.dp)
                .constrainAs(thumbnail) {
                    top.linkTo(
                        parent.top,
                        margin = 8.dp
                    )
                    start.linkTo(
                        parent.start,
                        margin = 8.dp
                    )
                    bottom.linkTo(parent.bottom)
                }
        )

        // title
        Text(
            text = video.name,
            modifier =
            Modifier.constrainAs(title) {
                top.linkTo(thumbnail.top, margin = 8.dp)
                start.linkTo(
                    thumbnail.end,
                    margin = 8.dp
                )
                end.linkTo(parent.end, margin = 8.dp)
                width = Dimension.preferredWrapContent
                height = Dimension.wrapContent
            },
            color = Color.Black,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            softWrap = true,
        )

        // divider
        Divider(
            modifier =
            Modifier
                .padding(horizontal = 8.dp)
                .testTag("Divider"),
            color = Color(0xFFE0E0E0)
        )

        // show only if video is currently playing
        if (currentlyPlaying.value) {
            // play button image
            Image(
                contentScale = ContentScale.Crop,
                colorFilter =
                if (video.preview.isEmpty())
                    ColorFilter.tint(Color.White)
                else
                    ColorFilter.tint(Color(0xFFF50057)),
                painter =
                painterResource(
                    id = android.R.drawable.ic_media_play
                ),
                contentDescription = "Playing",
                modifier =
                Modifier
                    .height(50.dp)
                    .width(50.dp)
                    .graphicsLayer {
                        clip = true
                        shadowElevation = 20.dp.toPx()
                    }
                    .constrainAs(play) {
                        top.linkTo(thumbnail.top)
                        start.linkTo(thumbnail.start)
                        end.linkTo(thumbnail.end)
                        bottom.linkTo(thumbnail.bottom)
                    }
            )

            // Now playing text
            Text(
                text = "Now Playing",
                color = Color(0xFFF50057),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier =
                Modifier.constrainAs(nowPlaying) {
                    top.linkTo(
                        title.bottom,
                        margin = 8.dp
                    )
                    start.linkTo(
                        thumbnail.end,
                        margin = 8.dp
                    )
                    bottom.linkTo(
                        thumbnail.bottom,
                        margin = 8.dp
                    )
                    end.linkTo(
                        parent.end,
                        margin = 8.dp
                    )
                    width =
                        Dimension.preferredWrapContent
                    height =
                        Dimension.preferredWrapContent
                }
            )
        }
    }
}

fun LazyListState.visibleItems(threshold: Float) =
    layoutInfo
        .visibleItemsInfo
        .filter { visibilityPercent(it) >= threshold }

fun LazyListState.visibilityPercent(info: LazyListItemInfo): Float {
    val cutTop = maxOf(0, layoutInfo.viewportStartOffset - info.offset)
    val cutBottom = maxOf(0, info.offset + info.size - layoutInfo.viewportEndOffset)

    return maxOf(0f, 100f - (cutTop + cutBottom) * 100f / info.size)
}