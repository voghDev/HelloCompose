package es.voghdev.hellocompose

import junit.framework.TestCase.assertEquals
import org.junit.Test

class DraggingStateTest {
    private val anOffset = 25f
    private val negativeOffset = -anOffset
    private val aNumberOfItems = 10

    @Test
    fun shouldReturnAPositiveOffsetWhenDraggingFirstCellToSixthPositionIfThereAreTenItems() {
        val state = givenTheUserIsDragging(fromIndex = 1, toIndex = 6)

        val offset = state.cellOffsetForMakingRoom(6, aNumberOfItems)

        assertEquals(anOffset, offset)
    }

    @Test
    fun shouldNotMoveFirstElementWhenDraggingIt() {
        val state = givenTheUserIsDragging(fromIndex = 0, toIndex = 0)

        val offset = state.cellOffsetForMakingRoom(0, aNumberOfItems)

        assertEquals(0f, offset)
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

    private fun givenTheUserIsDragging(fromIndex: Int, toIndex: Int): DraggingState =
        DraggingState().apply {
            draggedItemIndex = fromIndex
            droppedItemIndex = toIndex
            isDragging = true
        }
}