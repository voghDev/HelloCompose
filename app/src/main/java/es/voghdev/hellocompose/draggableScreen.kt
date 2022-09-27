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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter

data class Item(val title: String)

@Composable
fun DraggableScreen() {
    var isDragging by remember { mutableStateOf(false) }
    LongPressDraggable(modifier = Modifier.fillMaxSize()) {
        Column(Modifier.verticalScroll(rememberScrollState())) {
            (1..11).forEach { i ->
                DraggableItem(
                    item = Item("Item $i"),
                    onDrag = { isDragging = true },
                    onDragStarted = { isDragging = true },
                    onDragEnded = { isDragging = false },
                    isDragging = isDragging
                )
            }
        }

    }
}

@Composable
private fun DraggableItem(
    modifier: Modifier = Modifier,
    item: Item,
    onDrag: (Item) -> Unit,
    onDragStarted: (Item) -> Unit,
    onDragEnded: (Item) -> Unit,
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
    DragTarget(
        modifier = Modifier,
        dataToDrop = item,
        onDragStarted = onDragStarted,
        onDragEnded = onDragEnded,
        onDrag = onDrag
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(16.dp))
        )
    }

    DropTarget<Item>(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) { isInBound, _ ->
        val color = if (isInBound) {
            Color.Red
        } else {
            Color.Gray
        }
        Box(
            Modifier
                .width(88.dp)
                .height(24.dp)
                .padding(end = 48.dp)
                .align(Alignment.CenterEnd)
                .clip(RoundedCornerShape(16.dp))
                .background(color)
        )
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
