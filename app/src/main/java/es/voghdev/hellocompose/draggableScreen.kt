package es.voghdev.hellocompose

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollDispatcher
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun DraggableScreen() {
    val headerColors = listOf(Color.Blue, Color.Green, Color.Cyan)
    val bodyColors = listOf(Color.Red, Color.DarkGray, Color.Magenta)
    var currentPage by remember { mutableStateOf(0) }
    val pageCount = headerColors.size
    val pagerState = remember {
        PagerState(
            onPageSelected = { currentPage = it }
        )
    }
    pagerState.maxPage = pageCount.minus(1).coerceAtLeast(0)
    KPager(
        modifier = Modifier.fillMaxSize(),
        orientation = Orientation.Horizontal,
        state = pagerState
    ) {
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
}

@Composable
fun Pager(content: @Composable () -> Unit) {
    var pageSize by remember { mutableStateOf(0) }
    val orientation = Orientation.Horizontal
    val coroutineScope = rememberCoroutineScope()
    val nestedScrollDispatcher = remember { NestedScrollDispatcher() }

    Layout(content = {
        val pageData = Modifier
        Box(contentAlignment = Alignment.Center, modifier = pageData) {
            content()
        }
    }, modifier = Modifier
        .nestedScroll(connection = object : NestedScrollConnection {}, nestedScrollDispatcher)
        .draggable(
            orientation = orientation,
            onDragStarted = {
                Log.d("Draggable", "Starting drag")
            },
            onDragStopped = { velocity ->
                Log.d("Draggable", "Stopping drag")
                coroutineScope.launch {
                    val available = if (orientation == Orientation.Vertical) Velocity(0f, velocity)
                    else Velocity(velocity, 0f)
                    val consumed = nestedScrollDispatcher.dispatchPreFling(available)
                    nestedScrollDispatcher.dispatchPostFling(
                        Velocity.Zero,
                        consumed - available
                    )
                }
            },
            state = rememberDraggableState { dy ->
                val availableBeforeParent = if (orientation == Orientation.Vertical) Offset(0f, dy)
                else Offset(dy, 0f)
                val consumedByParents = nestedScrollDispatcher.dispatchPreScroll(
                    availableBeforeParent,
                    NestedScrollSource.Drag
                )
            }
        )
    ) { measurables, constraints ->
        layout(constraints.maxWidth, constraints.maxHeight) {
            val childConstraints = constraints.copy(minWidth = 0, minHeight = 0)
            measurables.map {
                it.measure(childConstraints) to it.pageNumber
            }.forEach { (placeable, page) ->
                val currentPage = 1
                val offset = 0f // state.currentPageOffset

                val xCenterOffset = (constraints.maxWidth - placeable.width) / 2
                val yCenterOffset = (constraints.maxHeight - placeable.height) / 2

                if (currentPage == page) {
                    pageSize = placeable.width
                }

                val xItemOffset = ((page + offset - currentPage) * placeable.width).roundToInt()

                placeable.place(
                    x = xCenterOffset + xItemOffset,
                    y = yCenterOffset
                )
            }
        }
    }
}

val Measurable.pageNumber: Int
    get() = 1