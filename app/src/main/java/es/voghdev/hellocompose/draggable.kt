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
    private val makeRoomOffset = 25f

    var isDragging: Boolean by mutableStateOf(false)
    var dragStartPosition by mutableStateOf(Offset.Zero)
    var dragOffset by mutableStateOf(Offset.Zero)
    var draggedItemIndex by mutableStateOf(0)
    var droppedItemIndex by mutableStateOf(0)
    var draggableComposable by mutableStateOf<(@Composable () -> Unit)?>(null)
    var itemBounds by mutableStateOf(mutableMapOf<Int, Rect>())
    var data by mutableStateOf<Any?>(null)

    fun clear() {
        isDragging = false
        dragStartPosition = Offset.Zero
        dragOffset = Offset.Zero
        draggedItemIndex = 0
        droppedItemIndex = 0
        draggableComposable = null
        itemBounds.clear()
        data = null
    }

    fun cellOffsetForMakingRoom(index: Int, numberOfItems: Int): Float {
        val previousIndex = maxOf(0, droppedItemIndex.minus(1))
        return when {
            draggedItemIndex != droppedItemIndex &&
                index == droppedItemIndex && index == numberOfItems.minus(1) -> -makeRoomOffset
            draggedItemIndex != droppedItemIndex &&
                index == droppedItemIndex && index == 0 -> makeRoomOffset
            index == draggedItemIndex && index == droppedItemIndex -> 0f
            index in (0..1) -> 0f
            index in (numberOfItems - 2 until numberOfItems) -> 0f
            isDragging && index == previousIndex -> -makeRoomOffset
            isDragging && index == droppedItemIndex -> makeRoomOffset
            else -> 0f
        }
    }

    fun indexForOffset(offset: Offset): Int {
        itemBounds[0]?.height?.takeIf { it > 0 }?.let { h ->
            val candidate = offset.y
                .div(h)
                .toInt()
            val candidates = listOf(
                candidate,
                maxOf(0, candidate.minus(1))
            )
            candidates.forEach { i ->
                if (itemBounds[i]?.contains(offset) == true) {
                    return i
                }
            }
        }
        itemBounds.forEach { entry ->
            if (entry.value.contains(offset)) {
                return entry.key
            }
        }
        return 0
    }
}

internal val LocalDraggingState = compositionLocalOf { DraggingState() }

@Composable
fun <T> Draggable(
    modifier: Modifier,
    data: T,
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
    val makeRoomAnimatedValue by animateFloatAsState(
        targetValue = currentState.cellOffsetForMakingRoom(index, numberOfItems)
    )

    Box(modifier = modifier
        .onGloballyPositioned {
            currentPosition = it.localToWindow(Offset.Zero)
            currentState.itemBounds[index] = it.boundsInWindow()
        }
        .pointerInput(Unit) {
            detectDragGesturesAfterLongPress(
                onDragStart = {
                    currentState.data = data
                    currentState.isDragging = true
                    currentState.draggedItemIndex = index
                    currentState.droppedItemIndex = index
                    currentState.dragStartPosition = currentPosition + it
                    currentState.draggableComposable = content
                    onDragStarted.invoke(data)
                }, onDrag = { change, dragAmount ->
                    change.consume()
                    currentState.dragOffset += Offset(dragAmount.x, dragAmount.y)
                    currentState.droppedItemIndex =
                        currentState.indexForOffset(currentState.dragStartPosition + currentState.dragOffset)
                    onDrag.invoke(data)
                }, onDragEnd = {
                    onDragEnded.invoke(data, maxOf(0, currentState.droppedItemIndex))
                    currentState.clear()
                }, onDragCancel = {
                    onDragCanceled.invoke(data)
                    currentState.clear()
                })
        }
        .graphicsLayer {
            translationY = makeRoomAnimatedValue
        }) {
        content()
    }
}

@Composable
fun VerticalDragAndDropContainer(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val state = remember { DraggingState() }
    CompositionLocalProvider(LocalDraggingState provides state) {
        Box(modifier = modifier.fillMaxSize())
        {
            content()
            if (state.isDragging) {
                DragAndDropGhostCell(state = state)
            }
        }
    }
}

@Composable
private fun DragAndDropGhostCell(state: DraggingState) {
    var targetSize by remember { mutableStateOf(IntSize.Zero) }
    Box(
        modifier = Modifier
            .onGloballyPositioned { targetSize = it.size }
            .graphicsLayer {
                val offset = (state.dragStartPosition + state.dragOffset)
                alpha = if (targetSize == IntSize.Zero) 0f else .9f
                translationY = offset.y.minus(targetSize.height)
            }
    ) {
        state.draggableComposable?.invoke()
    }
}
