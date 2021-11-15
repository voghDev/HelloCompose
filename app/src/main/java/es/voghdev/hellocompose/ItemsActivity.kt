package es.voghdev.hellocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import es.voghdev.hellocompose.ui.theme.HelloComposeTheme

class ItemsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelloComposeTheme {
                Column {
                    SampleRow()
                    SampleRow()
                    SampleRow()
                    SampleRow()
                }
            }
        }
    }

    @Preview
    @Composable
    private fun SampleRow() {
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
                RowBody(text = "This is a row")
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
            contentDescription = null
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
}
