package es.voghdev.hellocompose

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    private val someItems = (1..10).map { Item("Item $it") }
    private val anItem = Item("Item 9")
    private val anotherItem = Item("Item 2")

    @Test
    fun putsAnItemInTheRightPosition() {
        val result = someItems.withReallocatedItem(anItem, 1)

        assertEquals(anItem, result[1])
    }

    @Test
    fun removesTheItemAfterRepositioningIt() {
        val result = someItems.withReallocatedItem(anItem, 1)

        assertEquals(Item("Item 8"), result[8])
    }

    @Test
    fun returnsAListOfTheRightSize() {
        val result = someItems.withReallocatedItem(anItem, 1)

        assertEquals(10, result.size)
    }

    @Test
    fun returnsAListOfTheRightSizeWhenMovingToABiggerIndex() {
        val result = someItems.withReallocatedItem(anotherItem, 8)

        assertEquals(10, result.size)
    }

    @Test
    fun putsTheItemWhenIndexIsBigger() {
        val result = someItems.withReallocatedItem(anotherItem, 8)

        assertEquals(anotherItem, result[8])
    }

    @Test
    fun removesTheItemWhenIndexIsBigger() {
        val result = someItems.withReallocatedItem(anotherItem, 8)

        assertEquals(Item("Item 3"), result[1])
    }

    @Test
    fun putsTheItemInTheBeginningOfTheList() {
        val result = someItems.withReallocatedItem(anItem, 0)

        assertEquals(anItem, result[0])
    }

    @Test
    fun returnsTheRightSizeWhenAllocatingInTheFirstPosition() {
        val result = someItems.withReallocatedItem(anItem, 0)

        assertEquals(10, result.size)
    }

    @Test
    fun putsTheItemInTheEndOfTheList() {
        val result = someItems.withReallocatedItem(anotherItem, someItems.size - 1)

        assertEquals(anotherItem, result[result.size - 1])
    }

    @Test
    fun removesTheItemWhenAllocatingInTheEndOfTheList() {
        val result = someItems.withReallocatedItem(anotherItem, someItems.size - 1)

        assertEquals(Item("Item 3"), result[1])
    }

    @Test
    fun returnsTheSameListIfNewPositionIsTheSameAsCurrent() {
        val result = someItems.withReallocatedItem(anItem, 8)

        assertEquals(result, someItems)
    }

    @Test
    fun returnsTheSameListIfMovingTheFirstElementToTheSamePosition() {
        val result = someItems.withReallocatedItem(someItems.first(), 0)

        assertEquals(result, someItems)
    }

    @Test
    fun returnsTheSameListIfMovingTheLastElementToTheSamePosition() {
        val result = someItems.withReallocatedItem(someItems.last(), someItems.size - 1)

        assertEquals(result, someItems)
    }
}