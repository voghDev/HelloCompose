package es.voghdev.hellocompose

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

internal class DragTargetInfo {
    var isDragging: Boolean by mutableStateOf(false)
    var dragPosition by mutableStateOf(Offset.Zero)
    var dragOffset by mutableStateOf(Offset.Zero)
    var draggedItemIndex by mutableStateOf(0)
    var droppedItemIndex by mutableStateOf(0)
    var draggableComposable by mutableStateOf<(@Composable () -> Unit)?>(null)
    var dataToDrop by mutableStateOf<Any?>(null)
    var itemBounds by mutableStateOf(mutableMapOf<Int, Rect>())
}

internal val LocalDragTargetInfo = compositionLocalOf { DragTargetInfo() }

@Composable
fun <T> DragTarget(
    modifier: Modifier,
    dataToDrop: T,
    index: Int,
    onDragStarted: (T) -> Unit,
    onDragEnded: (T, Int) -> Unit,
    onDragCanceled: (T) -> Unit,
    onDrag: (T) -> Unit,
    content: @Composable (() -> Unit)
) {
    var currentPosition by remember { mutableStateOf(Offset.Zero) }
    val currentState = LocalDragTargetInfo.current

    Box(modifier = modifier
        .onGloballyPositioned {
            currentPosition = it.localToWindow(Offset.Zero)
            currentState.itemBounds[index] = it.boundsInWindow()
        }
        .pointerInput(Unit) {
            detectDragGesturesAfterLongPress(
                onDragStart = {
                    currentState.dataToDrop = dataToDrop
                    currentState.isDragging = true
                    currentState.draggedItemIndex = index
                    onDragStarted.invoke(dataToDrop)
                    currentState.dragPosition = currentPosition + it
                    currentState.draggableComposable = content
                }, onDrag = { change, dragAmount ->
                    change.consume()
                    onDrag.invoke(dataToDrop)
                    currentState.dragOffset += Offset(dragAmount.x, dragAmount.y)
                    val pos = currentState.dragPosition + currentState.dragOffset
                    currentState.itemBounds.forEach { entry -> // TODO find a more optimal structure!
                        if (entry.value.contains(pos) && entry.key != index) {
                            currentState.droppedItemIndex = entry.key
                        }
                    }
                }, onDragEnd = {
                    currentState.isDragging = false
                    currentState.dragOffset = Offset.Zero
                    onDragEnded.invoke(dataToDrop, maxOf(0, currentState.droppedItemIndex))
                }, onDragCancel = {
                    currentState.dragOffset = Offset.Zero
                    currentState.isDragging = false
                    onDragCanceled.invoke(dataToDrop)
                })
        }
        .graphicsLayer {
            val hoveredIndex = currentState.droppedItemIndex
            val previousIndex = maxOf(0, hoveredIndex.minus(1))
            translationY = when {
                index == currentState.draggedItemIndex -> 0f
                currentState.isDragging && index == previousIndex -> -15f
                currentState.isDragging && index == hoveredIndex -> 15f
                else -> 0f
            }
        }) {
        content()
    }
}

@Composable
fun LongPressDraggable(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val state = remember { DragTargetInfo() }
    CompositionLocalProvider(
        LocalDragTargetInfo provides state
    ) {
        Box(modifier = modifier.fillMaxSize())
        {
            content()
            if (state.isDragging) {
                var targetSize by remember {
                    mutableStateOf(IntSize.Zero)
                }
                Box(modifier = Modifier
                    .onGloballyPositioned {
                        targetSize = it.size
                    }
                    .graphicsLayer {
                        val offset = (state.dragPosition + state.dragOffset)
                        scaleX = 1f
                        scaleY = 1f
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
