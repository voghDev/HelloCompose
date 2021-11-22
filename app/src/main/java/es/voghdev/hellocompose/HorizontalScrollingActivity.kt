package es.voghdev.hellocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import es.voghdev.hellocompose.ui.theme.HelloComposeTheme

class HorizontalScrollingActivity : ComponentActivity() {
    val x = listOf(
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
            items(x) {
                SimpleItem(it)
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

    data class Item(val title: String, val description: String)
}