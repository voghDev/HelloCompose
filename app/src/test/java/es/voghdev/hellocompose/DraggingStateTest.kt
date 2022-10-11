package es.voghdev.hellocompose

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import junit.framework.TestCase.assertEquals
import org.junit.Test

class DraggingStateTest {
    private val anOffset = 25f
    private val negativeOffset = -anOffset
    private val aNumberOfItems = 10

    @Test
    fun shouldReturnAPositiveOffsetWhenDraggingSecondCellToSixthPositionIfThereAreTenItems() {
        val state = givenTheUserIsDragging(fromIndex = 1, toIndex = 6)

        val offset = state.cellOffsetForMakingRoom(6, aNumberOfItems)

        assertEquals(negativeOffset, offset)
    }

    @Test
    fun shouldNotMoveFirstElementWhenDraggingIt() {
        val state = givenTheUserIsDragging(fromIndex = 0, toIndex = 0)

        val offset = state.cellOffsetForMakingRoom(0, aNumberOfItems)

        assertEquals(0f, offset)
    }

    @Test
    fun shouldMoveSecondCellWhenDraggingFifthCellToTheFirstOne() {
        val state = givenTheUserIsDragging(fromIndex = 5, toIndex = 6)

        val offset = state.cellOffsetForMakingRoom(6, aNumberOfItems)

        assertEquals(negativeOffset, offset)
    }

    @Test
    fun shouldNotMoveLastElementWhenDraggingIt() {
        val state = givenTheUserIsDragging(fromIndex = 9, toIndex = 9)

        val offset = state.cellOffsetForMakingRoom(9, aNumberOfItems)

        assertEquals(0f, offset)
    }

    @Test
    fun shouldMoveLastElementWhenDraggingAnElementInTheMiddleToIt() {
        val state = givenTheUserIsDragging(fromIndex = 6, toIndex = 9)

        val offset = state.cellOffsetForMakingRoom(9, aNumberOfItems)

        assertEquals(negativeOffset, offset)
    }

    @Test
    fun shouldMoveFirstElementWhenDraggingAnElementInTheMiddleToIt() {
        val state = givenTheUserIsDragging(fromIndex = 6, toIndex = 0)

        val offset = state.cellOffsetForMakingRoom(0, aNumberOfItems)

        assertEquals(anOffset, offset)
    }

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

    @Test
    fun shouldMoveCell5UpWhenDraggingCell4OnTopOfIt() {
        val state = givenTheUserIsDragging(fromIndex = 4, toIndex = 5)

        val offset = state.cellOffsetForMakingRoom(5, aNumberOfItems)

        assertEquals(negativeOffset, offset)
    }

    @Test
    fun shouldMoveBeforeLastCellWhenDraggingCell7ToIt() {
        val state = givenTheUserIsDragging(fromIndex = 7, toIndex = 8)

        val offset = state.cellOffsetForMakingRoom(8, aNumberOfItems)

        assertEquals(negativeOffset, offset)
    }

    private fun givenTheUserIsDragging(fromIndex: Int, toIndex: Int): DraggingState =
        DraggingState().apply {
            draggedItemIndex = fromIndex
            droppedItemIndex = toIndex
            isDragging = true
        }
}