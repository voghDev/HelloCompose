package es.voghdev.hellocompose

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize

internal class DraggingState {
    var isDragging: Boolean by mutableStateOf(false)
    var dragPosition by mutableStateOf(Offset.Zero)
    var dragOffset by mutableStateOf(Offset.Zero)
    var draggedItemIndex by mutableStateOf(0)
    var droppedItemIndex by mutableStateOf(0)
    var draggableComposable by mutableStateOf<(@Composable () -> Unit)?>(null)
    var itemBounds by mutableStateOf(mutableMapOf<Int, Rect>())
    var data by mutableStateOf<Any?>(null)

    fun clear() {
        isDragging = false
        dragPosition = Offset.Zero
        dragOffset = Offset.Zero
        draggedItemIndex = 0
        droppedItemIndex = 0
        draggableComposable = null
        itemBounds.clear()
        data = null
    }
}

internal val LocalDraggingState = compositionLocalOf { DraggingState() }
const val makingRoomOffset = 25f

@Composable
fun <T> Draggable(
    modifier: Modifier,
    dataToDrop: T,
    index: Int,
    numberOfItems: Int,
    onDragStarted: (T) -> Unit,
    onDragEnded: (T, Int) -> Unit,
    onDragCanceled: (T) -> Unit,
    onDrag: (T) -> Unit,
    content: @Composable (() -> Unit)
) {
    var currentPosition by remember { mutableStateOf(Offset.Zero) }
    val currentState = LocalDraggingState.current
    val hoveredIndex = currentState.droppedItemIndex
    val previousIndex = maxOf(0, hoveredIndex.minus(1))
    val offsetForMakingRoom = when {
        index == currentState.draggedItemIndex -> 0f
        index in listOf(0, 1) -> 0f
        index in listOf(numberOfItems - 2, numberOfItems - 1) -> 0f
        currentState.isDragging && index == previousIndex -> -makingRoomOffset
        currentState.isDragging && index == hoveredIndex -> makingRoomOffset
        else -> 0f
    }
    val makeRoomAnimatedValue by animateFloatAsState(targetValue = offsetForMakingRoom)

    Box(modifier = modifier
        .onGloballyPositioned {
            currentPosition = it.localToWindow(Offset.Zero)
            currentState.itemBounds[index] = it.boundsInWindow()
        }
        .pointerInput(Unit) {
            detectDragGesturesAfterLongPress(
                onDragStart = {
                    currentState.data = dataToDrop
                    currentState.isDragging = true
                    currentState.draggedItemIndex = index
                    currentState.dragPosition = currentPosition + it
                    currentState.draggableComposable = content
                    onDragStarted.invoke(dataToDrop)
                }, onDrag = { change, dragAmount ->
                    change.consume()
                    currentState.dragOffset += Offset(dragAmount.x, dragAmount.y)
                    var found = false
                    val pos = currentState.dragPosition + currentState.dragOffset
                    currentState.itemBounds[0]?.height?.let { h ->
                        val candidate = pos.y
                            .div(h)
                            .toInt()
                        val candidates = listOf(
                            candidate,
                            maxOf(0, candidate.minus(1))
                        )
                        candidates.forEach { i ->
                            if (currentState.itemBounds[i]?.contains(pos) == true) {
                                currentState.droppedItemIndex = i
                                found = true
                            }
                        }
                    }
                    if (!found) {
                        currentState.itemBounds.forEach { entry ->
                            if (entry.value.contains(pos)) {
                                currentState.droppedItemIndex = entry.key
                                found = true
                            }
                            if (found) {
                                found = false
                                return@forEach
                            }
                        }
                    }
                    onDrag.invoke(dataToDrop)
                }, onDragEnd = {
                    currentState.clear()
                    onDragEnded.invoke(dataToDrop, maxOf(0, currentState.droppedItemIndex))
                }, onDragCancel = {
                    currentState.clear()
                    onDragCanceled.invoke(dataToDrop)
                })
        }
        .graphicsLayer {
            translationY = makeRoomAnimatedValue
        }) {
        content()
    }
}

@Composable
fun DraggableContainer(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val state = remember { DraggingState() }
    CompositionLocalProvider(LocalDraggingState provides state) {
        Box(modifier = modifier.fillMaxSize())
        {
            content()
            if (state.isDragging) {
                var targetSize by remember { mutableStateOf(IntSize.Zero) }
                Box(modifier = Modifier
                    .onGloballyPositioned { targetSize = it.size }
                    .graphicsLayer {
                        val offset = (state.dragPosition + state.dragOffset)
                        alpha = if (targetSize == IntSize.Zero) 0f else .9f
                        translationY = offset.y.minus(targetSize.height)
                    }
                ) {
                    state.draggableComposable?.invoke()
                }
            }
        }
    }
}
