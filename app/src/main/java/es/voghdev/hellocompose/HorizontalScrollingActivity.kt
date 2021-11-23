package es.voghdev.hellocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import es.voghdev.hellocompose.ui.theme.HelloComposeTheme

class HorizontalScrollingActivity : ComponentActivity() {
    private val items = listOf(
        Item("Hi", "This is an item"),
        Item("Hello", "This is another item"),
        Item("Hey", "This is another item"),
        Item("Hi!", "This is a very very very long item"),
        Item("And", "This is the third item")
    )
    private val persons = listOf(
        Person("William Hunt"),
        Person("Adelle Sophie"),
        Person("Chris Brown"),
        Person("Dan Dom"),
        Person("Walter Heinz"),
        Person("James Mack")
    )
    private val cardWidth = 96.dp
    private val cardHeight = 148.dp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelloComposeTheme {
                ItemsList()
            }
        }
    }

    @Composable
    fun ItemsList() {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .background(Color.LightGray)
        ) {
            items(items) {
                SimpleItem(it)
            }
            item {
                LazyRow(
                    Modifier
                        .fillMaxWidth()
                        .height(cardHeight)
                ) {
                    item {
                        val personName = "Aaron Ward"
                        val pictureUrl = "https://via.placeholder.com/64x64.png?text=${
                            personName.replace(
                                " ",
                                "+"
                            )
                        }"
                        PersonCard(
                            pictureUrl = pictureUrl,
                            name = personName
                        )
                    }
                    itemsIndexed(persons) { i, it ->
                        val realPictureUrl = "https://lorempixel.com/64/64/people/$i/"
                        PersonCard(pictureUrl = realPictureUrl, name = it.name)
                    }
                }
            }
            items(items) {
                SimpleItem(it)
            }
        }
    }

    @Preview
    @Composable
    private fun PersonPreview() {
        PersonCard(
            cardWidth,
            cardHeight,
            "https://via.placeholder.com/64x64.png?text=Sample+Image",
            "Aaron Ward"
        )
    }

    @Composable
    fun PersonCard(
        width: Dp = cardWidth,
        height: Dp = cardHeight,
        pictureUrl: String,
        name: String
    ) {
        Box(Modifier.padding(start = 4.dp, end = 4.dp)) {
            RoundedCornersBackground(width, cardHeight)
            Column(
                Modifier
                    .width(width)
                    .height(height)
            ) {
                Image(
                    painter = rememberImagePainter(
                        data = pictureUrl,
                        builder = {
                            transformations(CircleCropTransformation())
                        }
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(64.dp)
                        .padding(top = 8.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Text(
                    name,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .align(Alignment.CenterHorizontally),
                    maxLines = 1,
                    fontSize = 11.sp
                )
                Button(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .align(Alignment.CenterHorizontally),
                    onClick = { /*TODO*/ }) {
                    Text("Follow", fontSize = 11.sp)
                }
            }
        }
    }

    @Composable
    private fun RoundedCornersBackground(width: Dp, height: Dp) {
        Box(
            Modifier
                .background(shape = RoundedCornerShape(16.dp), color = Color.White)
                .width(width)
                .height(height)
        )
    }

    @Composable
    fun SimpleItem(item: Item) {
        BoxWithConstraints(
            Modifier
                .fillMaxWidth()
                .height(64.dp)
        ) {
            val constraints = ConstraintSet {
                val title = createRefFor("title")
                val description = createRefFor("description")

                constrain(title) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                constrain(description) {
                    top.linkTo(title.bottom)
                    start.linkTo(title.start)
                }
            }

            ConstraintLayout(constraints) {
                Text(
                    item.title, modifier = Modifier
                        .layoutId("title")
                        .padding(start = 16.dp, top = 8.dp),
                    fontSize = 16.sp
                )
                Text(
                    item.description,
                    modifier = Modifier
                        .layoutId("description")
                        .padding(start = 16.dp),
                    fontSize = 14.sp
                )
            }
        }
    }

    data class Item(val title: String, val description: String)

    data class Person(val name: String)
}
