package es.voghdev.hellocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import es.voghdev.hellocompose.ui.theme.HelloComposeTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      HelloComposeTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
          Greeting("Compose", Modifier.firstBaselineToTop(32.dp))
          Greeting("Regular padding", Modifier.padding(top = 32.dp, start = 100.dp))
          CustomColumn {
            Text("This is a custom column")
            Text("Written using Compose Codelab")
          }
        }
      }
    }
  }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(text = "Hello $name!", modifier)
}

@Composable
fun CustomColumn(
  modifier: Modifier = Modifier,
  // custom layout attributes
  content: @Composable () -> Unit
) {
  Layout(
    modifier = modifier,
    content = content
  ) { measurables, constraints ->
    val placeables = measurables.map { measurable ->
      measurable.measure(constraints)
    }

    var yPosition = 0

    layout(constraints.maxWidth, constraints.maxHeight) {
      placeables.forEach { placeable ->

        placeable.placeRelative(x = 0, y = yPosition)

        yPosition += placeable.height
      }
    }
  }
}
