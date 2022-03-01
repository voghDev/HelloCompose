package es.voghdev.hellocompose

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollDispatcher
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Velocity
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class PagerState(
    currentPage: Int = 0,
    minPage: Int = 0,
    maxPage: Int = 0,
    val onPageSelected: (Int) -> Unit
) {
    private var _minPage by mutableStateOf(minPage)
    var minPage: Int
        get() = _minPage
        set(value) {
            _minPage = value.coerceAtMost(_maxPage)
            _currentPage = _currentPage.coerceIn(_minPage, _maxPage)
        }

    private var _maxPage by mutableStateOf(maxPage, structuralEqualityPolicy())
    var maxPage: Int
        get() = _maxPage
        set(value) {
            _maxPage = value.coerceAtLeast(_minPage)
            _currentPage = _currentPage.coerceIn(_minPage, maxPage)
        }

    private var _currentPage by mutableStateOf(currentPage.coerceIn(minPage, maxPage))
    var currentPage: Int
        get() = _currentPage
        set(value) {
            _currentPage = value.coerceIn(minPage, maxPage)
        }

    enum class SelectionState { Selected, Undecided }

    var selectionState by mutableStateOf(SelectionState.Selected)

    suspend inline fun <R> selectPage(block: PagerState.() -> R): R = try {
        selectionState = SelectionState.Undecided
        block()
    } finally {
        selectPage()
    }

    suspend fun selectPage() {
        currentPage -= currentPageOffset.roundToInt()
        snapToOffset(0f)
        selectionState = SelectionState.Selected
        onPageSelected(currentPage)
    }

    private var _currentPageOffset = Animatable(0f).apply {
        updateBounds(-1f, 1f)
    }
    val currentPageOffset: Float
        get() = _currentPageOffset.value

    suspend fun snapToOffset(offset: Float) {
        val max = if (currentPage == minPage) 0f else 1f
        val min = if (currentPage == maxPage) 0f else -1f
        _currentPageOffset.snapTo(offset.coerceIn(min, max))
    }

    suspend fun fling(velocity: Float) {
        if (velocity < 0 && currentPage == maxPage) return
        if (velocity > 0 && currentPage == minPage) return
        _currentPageOffset.animateDecay(velocity, animationSpec = exponentialDecay())
        _currentPageOffset.animateTo(currentPageOffset.roundToInt().toFloat())
        selectPage()
    }

    override fun toString(): String = "PagerState{minPage=$minPage, maxPage=$maxPage, " +
        "currentPage=$currentPage, currentPageOffset=$currentPageOffset}"
}

@Immutable
private data class PageData(val page: Int) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?): Any = this@PageData
}

private val Measurable.page: Int
    get() = (parentData as? PageData)?.page ?: error("no PageData for measurable $this")

@Composable
@Suppress("LongMethod")
fun KPager(
    state: PagerState,
    modifier: Modifier = Modifier,
    offscreenLimit: Int = 2,
    orientation: Orientation = Orientation.Horizontal,
    content: @Composable PagerScope.() -> Unit
) {
    var pageSize by remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()
    val nestedScrollDispatcher = remember { NestedScrollDispatcher() }
    Layout(
        content = {
            val minPage = (state.currentPage - offscreenLimit).coerceAtLeast(state.minPage)
            val maxPage = (state.currentPage + offscreenLimit).coerceAtMost(state.maxPage)

            for (page in minPage..maxPage) {
                val pageData = PageData(page)
                val scope = PagerScope(state, page)
                key(pageData) {
                    Box(contentAlignment = Alignment.Center, modifier = pageData) {
                        scope.content()
                    }
                }
            }
        },
        modifier = modifier
            .nestedScroll(connection = object : NestedScrollConnection {}, nestedScrollDispatcher)
            .draggable(
                orientation = orientation,
                onDragStarted = {
                    state.selectionState = PagerState.SelectionState.Undecided
                },
                onDragStopped = { velocity ->

                    coroutineScope.launch {
                        // Velocity is in pixels per second, but we deal in percentage offsets, so we
                        // need to scale the velocity to match
                        state.fling((velocity / pageSize) * 2)
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
                    coroutineScope.launch {
                        with(state) {
                            val pos = pageSize * currentPageOffset
                            val max = if (currentPage == minPage) 0 else pageSize * offscreenLimit
                            val min = if (currentPage == maxPage) 0 else -pageSize * offscreenLimit
                            val newPos = (pos + dy).coerceIn(min.toFloat(), max.toFloat())
                            snapToOffset(newPos / pageSize)
                            if (currentPage == 0) {
                                val availableAfterParents =
                                    availableBeforeParent - consumedByParents
                                nestedScrollDispatcher.dispatchPostScroll(
                                    consumed = consumedByParents,
                                    available = availableAfterParents,
                                    source = NestedScrollSource.Drag
                                )
                            }
                        }
                    }
                },
            )
    ) { measurables, constraints ->
        layout(constraints.maxWidth, constraints.maxHeight) {
            val currentPage = state.currentPage
            val offset = state.currentPageOffset
            val childConstraints = constraints.copy(minWidth = 0, minHeight = 0)

            measurables
                .map {
                    it.measure(childConstraints) to it.page
                }
                .forEach { (placeable, page) ->
                    val xCenterOffset = (constraints.maxWidth - placeable.width) / 2
                    val yCenterOffset = (constraints.maxHeight - placeable.height) / 2

                    if (currentPage == page) {
                        pageSize = placeable.width
                    }

                    val xItemOffset = ((page + offset - currentPage) * placeable.width).roundToInt()
                    val yItemOffset = ((page + offset - currentPage) * placeable.height).roundToInt()

                    if (orientation == Orientation.Horizontal) {
                        placeable.place(
                            x = xCenterOffset + xItemOffset,
                            y = yCenterOffset
                        )
                    } else {
                        placeable.place(
                            x = xCenterOffset,
                            y = yCenterOffset + yItemOffset
                        )
                    }
                }
        }
    }
}

/**
 * Scope for [Pager] content.
 */
class PagerScope(
    private val state: PagerState,
    val page: Int
) {
    /**
     * Returns the current selected page
     */
    val currentPage: Int
        get() = state.currentPage

    /**
     * Returns the current selected page offset
     */
    val currentPageOffset: Float
        get() = state.currentPageOffset

    /**
     * Returns the current selection state
     */
    val selectionState: PagerState.SelectionState
        get() = state.selectionState
}
