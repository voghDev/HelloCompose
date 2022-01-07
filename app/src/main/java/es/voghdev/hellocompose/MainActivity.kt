package es.voghdev.hellocompose

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import es.voghdev.hellocompose.ui.theme.HelloComposeTheme

class MainActivity : ComponentActivity() {
    data class Video(
        val id: String,
        val name: String,
        val video: String
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelloComposeTheme {
                VideoPlayer()
            }
        }
        startActivity(Intent(this, ItemsActivity::class.java))
    }
}

@Composable
fun VideoPlayer(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val videos = listOf(
        MainActivity.Video(
            "001",
            "Big buck bunny",
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
        ),
        MainActivity.Video(
            "002",
            "Sintel",
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4"
        ),
    )
    val mediaItems = arrayListOf<MediaItem>()

    // create MediaItem
    videos.forEach {
        mediaItems.add(
            MediaItem.Builder()
                .setUri(it.video)
                .setMediaId(it.id)
                .setTag(it)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setDisplayTitle(it.name)
                        .build()
                )
                .build()
        )
    }

    val exoPlayer = remember {
        SimpleExoPlayer.Builder(context).build().apply {
            this.setMediaItems(mediaItems)
            this.prepare()
            this.playWhenReady = false
        }
    }

    ConstraintLayout(modifier = modifier) {
        val (title, videoPlayer) = createRefs()

        // video title
        Text(
            text = "Current Title",
            color = Color.White,
            modifier =
            Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        // player view
        DisposableEffect(
            AndroidView(
                modifier =
                Modifier
                    .testTag("VideoPlayer")
                    .constrainAs(videoPlayer) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
                factory = {

                    // exo player view for our video player
                    PlayerView(context).apply {
                        player = exoPlayer
                        layoutParams =
                            FrameLayout.LayoutParams(
                                ViewGroup.LayoutParams
                                    .MATCH_PARENT,
                                ViewGroup.LayoutParams
                                    .MATCH_PARENT
                            )
                    }
                }
            )
        ) {
            onDispose {
                // relase player when no longer needed
                exoPlayer.release()
            }
        }
    }
}