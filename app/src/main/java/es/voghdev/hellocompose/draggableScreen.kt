package es.voghdev.hellocompose

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter

data class Item(val title: String)

private val itemsList = (0..9).map { Item("Item $it") }
fun <T> List<T>.withReallocatedItem(updatedItem: T, newPosition: Int): List<T> {
    val index = this.indexOf(updatedItem)

    return if (index > newPosition) {
        subList(0, newPosition)
            .plus(updatedItem)
            .plus(subList(newPosition, index))
            .plus(subList(index + 1, size))
    } else {
        subList(0, index)
            .plus(subList(index + 1, newPosition + 1))
            .plus(updatedItem)
            .plus(subList(newPosition + 1, size))
    }
}

@Composable
fun DraggableScreen() {
    var isDragging by remember { mutableStateOf(false) }
    var itemsState by remember { mutableStateOf(itemsList) }
    DraggableContainer(modifier = Modifier.fillMaxSize()) {
        key(itemsState) {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                itemsState.forEachIndexed { i, item ->
                    Draggable(
                        modifier = Modifier,
                        dataToDrop = item,
                        index = i,
                        onDrag = { isDragging = true },
                        onDragStarted = {
                            isDragging = true
                        },
                        onDragCanceled = { isDragging = false },
                        onDragEnded = { item, newPosition ->
                            isDragging = false
                            itemsState = itemsState.withReallocatedItem(item, newPosition)
                        }
                    ) {
                        DraggableItem(
                            item = item,
                            isDragging = isDragging
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DraggableItem(
    modifier: Modifier = Modifier,
    item: Item,
    isDragging: Boolean
) = Box(modifier) {
    HighlightedBackground()
    Column(Modifier.clickable(onClick = { })) {
        SmallSpacer()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    bottom = 8.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SampleImage()
            SmallSpacer()
            RowTitle(modifier = Modifier.weight(1f), text = item.title)
            if (isDragging) {
                MinSpacer()
                ReorderIcon()
            } else {
                SmallSpacer()
            }
        }
        Separator()
    }
}

@Composable
private fun SmallSpacer() = Spacer(modifier = Modifier.size(8.dp))

@Composable
private fun MinSpacer() = Spacer(modifier = Modifier.size(4.dp))

@Composable
private fun SampleImage() = Box(Modifier.size(48.dp)) {
    Image(
        painter = rememberImagePainter(data = "https://lorempixel.com/48/48/people/1/"),
        contentDescription = null,
        modifier = Modifier.padding(start = 16.dp, top = 16.dp)
    )
}

@Composable
private fun RowTitle(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Black
) = Text(
    text = text,
    style = MaterialTheme.typography.body2,
    color = color,
    modifier = modifier,
    lineHeight = 22.sp,
    overflow = TextOverflow.Ellipsis
)

@Composable
private fun Separator() = Box(
    Modifier
        .fillMaxWidth()
        .padding(
            start = 8.dp,
            end = 8.dp
        )
        .height(1.dp)
        .background(MaterialTheme.colors.onPrimary)
)

@Composable
private fun ReorderIcon() = Box(Modifier) {
    Image(
        painter = painterResource(id = R.drawable.ic_reorder),
        contentDescription = null
    )
}

@Composable
private fun HighlightedBackground() = Card(
    backgroundColor = Color.LightGray,
    shape = RoundedCornerShape(16.dp),
    modifier = Modifier
        .fillMaxWidth()
        .height(64.dp)
        .padding(start = 12.dp, end = 12.dp)
) {}
