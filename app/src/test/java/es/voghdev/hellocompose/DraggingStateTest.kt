package es.voghdev.hellocompose

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import junit.framework.TestCase.assertEquals
import org.junit.Test

class DraggingStateTest {
    @Test
    fun shouldNotCrashWhenHeightDefinedInBoundsIsZero() {
        val bounds = mutableMapOf(
            0 to Rect(Offset.Zero, Offset.Zero),
            1 to Rect(Offset.Zero, Offset(100f, 100f)),
        )
        val state = givenTheUserIsDragging(fromIndex = 0, toIndex = 0)

        val result = state.indexForOffset(bounds, Offset(10f, 10f))

        assertEquals(1, result)
    }

    @Test
    fun shouldReturnZeroWhenBoundsAreNotFound() {
        val bounds = mutableMapOf(
            0 to Rect(Offset.Zero, Offset.Zero),
            1 to Rect(Offset.Zero, Offset(100f, 100f)),
        )
        val state = givenTheUserIsDragging(fromIndex = 0, toIndex = 0)

        val result = state.indexForOffset(bounds, Offset(150f, 150f))

        assertEquals(0, result)
    }

    private fun givenTheUserIsDragging(fromIndex: Int, toIndex: Int): DraggingState =
        DraggingState().apply {
            draggedItemIndex = fromIndex
            droppedItemIndex = toIndex
            isDragging = true
        }
}