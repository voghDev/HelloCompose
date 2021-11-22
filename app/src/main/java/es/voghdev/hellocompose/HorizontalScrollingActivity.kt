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
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import coil.compose.rememberImagePainter
import es.voghdev.hellocompose.ui.theme.HelloComposeTheme

class HorizontalScrollingActivity : ComponentActivity() {
    val items = listOf(
        Item("Hi", "This is an item"),
        Item("Hello", "This is another item"),
        Item("Bye", "This is the third item")
    )

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
        LazyColumn(Modifier.fillMaxSize()) {
            items(items) {
                SimpleItem(it)
            }
            item {
                LazyRow(
                    Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(Color.Yellow)
                ) {
                    item {
                        PersonCard(
                            pictureUrl = "\"https://lorempixel.com/48/48/people/1/\"",
                            name = "Aaron Ward"
                        )
                    }
                }
            }
        }
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
                Text(item.title, modifier = Modifier.layoutId("title"))
                Text(item.description, modifier = Modifier.layoutId("description"))
            }
        }
    }

    @Composable
    fun PersonCard(pictureUrl: String, name: String) {
        Box {
            Image(
                painter = rememberImagePainter(data = pictureUrl),
                contentDescription = null,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(name, modifier = Modifier.padding(top = 88.dp))
            Button(modifier = Modifier.padding(top = 102.dp), onClick = { /*TODO*/ }) {
                Text("Follow")
            }
        }
    }

    data class Item(val title: String, val description: String)
}
