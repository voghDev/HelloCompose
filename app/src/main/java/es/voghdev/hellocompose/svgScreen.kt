package es.voghdev.hellocompose

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import es.voghdev.hellocompose.ui.theme.Shapes

@Composable
fun SvgScreen() {
    Box(
        Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.teal_200))
    ) {
        Box(
            Modifier
                .size(120.dp)
                .align(Alignment.BottomStart)
                .background(colorResource(id = R.color.teal_700))
                .clickable { Log.d("", "Click on the left background") }
        ) {
            val buttonShape = Shapes.small
            IconButton(
                modifier = Modifier
                    .padding(top = 12.dp, start = 12.dp)
                    .size(32.dp)
                    .background(colorResource(id = R.color.grey), buttonShape)
                    .clip(buttonShape),
                onClick = { Log.d("", "Click on the left button") }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_drop_down),
                    tint = Color.White,
                    contentDescription = "SVG icon"
                )
            }
        }

        Box(
            Modifier
                .size(120.dp)
                .align(Alignment.BottomEnd)
                .background(colorResource(id = R.color.purple_500))
                .clickable { Log.d("", "Click on the right background") }
        ) {
            val buttonShape = Shapes.small
            IconButton(
                modifier = Modifier
                    .padding(top = 12.dp, end = 12.dp)
                    .align(Alignment.TopEnd)
                    .size(32.dp)
                    .background(colorResource(id = R.color.grey), buttonShape)
                    .clip(buttonShape),
                onClick = { Log.d("", "Click on the right button") }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_drop_down_filled),
                    tint = Color.Unspecified,
                    contentDescription = "SVG icon filled"
                )
            }
        }
    }
}