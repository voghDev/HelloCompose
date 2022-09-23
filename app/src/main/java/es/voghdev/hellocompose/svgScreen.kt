package es.voghdev.hellocompose

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import es.voghdev.hellocompose.ui.theme.Shapes

enum class CentralShapeStatus(val height: Int) {
    Dragging(-1), Hidden(0), Collapsed(78), Expanded(280)
}

@Composable
fun SvgScreen() {
    var centralShapeHeight = remember { mutableStateOf(CentralShapeStatus.Expanded.height) }
    var centralShapeStatus by remember { mutableStateOf(CentralShapeStatus.Expanded) }
    val transition = updateTransition(targetState = centralShapeHeight, label = "updateTransition")

    val shapeHeight by transition.animateDp(label = "transitionDp") { it.value.dp }

    Box(
        Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.teal_200))
    ) {
        Box(
            Modifier
                .height(shapeHeight)
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = spring(Spring.DampingRatioNoBouncy, Spring.StiffnessHigh),
                    finishedListener = { _, _ ->
                        centralShapeStatus = when (centralShapeHeight.value) {
                            CentralShapeStatus.Collapsed.height -> CentralShapeStatus.Collapsed
                            CentralShapeStatus.Expanded.height -> CentralShapeStatus.Expanded
                            else -> CentralShapeStatus.Hidden
                        }
                    })
                .draggable(
                    orientation = Orientation.Vertical,
                    onDragStarted = {
                        centralShapeStatus = CentralShapeStatus.Dragging
                    },
                    state = rememberDraggableState { delta ->
                        centralShapeHeight.value = when {
                            delta > 0 -> CentralShapeStatus.Collapsed.height
                            delta < 0 -> CentralShapeStatus.Expanded.height
                            else -> centralShapeHeight.value
                        }
                    }
                )
                .align(Alignment.BottomCenter)
                .background(
                    colorResource(
                        id = R.color.purple_200
                    )
                )
        ) {
            HideCentralShapeButton(centralShapeHeight)
            StatusLabel(centralShapeStatus)
        }

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
                onClick = {
                    centralShapeHeight.value = CentralShapeStatus.Expanded.height
                    Log.d("", "Click on the left button")
                }
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
                .clickable {
                    Log.d("", "Click on the right background")
                }
        ) {
            val buttonShape = Shapes.small
            IconButton(
                modifier = Modifier
                    .padding(top = 12.dp, end = 12.dp)
                    .align(Alignment.TopEnd)
                    .size(32.dp)
                    .background(colorResource(id = R.color.grey), buttonShape)
                    .clip(buttonShape),
                onClick = {
                    centralShapeHeight.value = CentralShapeStatus.Collapsed.height
                    Log.d("", "Click on the right button")
                }
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

@Composable
private fun BoxScope.HideCentralShapeButton(centralShapeHeight: MutableState<Int>) =
    IconButton(
        modifier = Modifier
            .padding(top = 12.dp, start = 12.dp)
            .size(32.dp)
            .align(Alignment.TopCenter)
            .background(colorResource(id = R.color.teal_700)),
        onClick = {
            centralShapeHeight.value = CentralShapeStatus.Hidden.height
        }
    ) {
        Icon(
            painter = painterResource(id = android.R.drawable.ic_menu_close_clear_cancel),
            tint = Color.White,
            contentDescription = "Close"
        )
    }

@Composable
private fun BoxScope.StatusLabel(status: CentralShapeStatus) {
    Text(
        modifier = Modifier
            .align(Alignment.TopCenter)
            .padding(top = 36.dp),
        text = status.toString()
    )
}
