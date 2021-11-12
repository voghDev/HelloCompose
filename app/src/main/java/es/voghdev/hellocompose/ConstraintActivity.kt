package es.voghdev.hellocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import es.voghdev.hellocompose.ui.theme.HelloComposeTheme

class ConstraintActivity: ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      HelloComposeTheme {
        ConstraintLayoutContent()
      }
    }
  }

  @Composable
  fun ConstraintLayoutContent() {
    ConstraintLayout {
      val (button, text) = createRefs()

      Button(
        onClick = { /* Do something */ },
        // Assign reference "button" to the Button composable
        // and constrain it to the top of the ConstraintLayout
        modifier = Modifier.constrainAs(button) {
          top.linkTo(parent.top, margin = 16.dp)
        }
      ) {
        Text("Button")
      }

      // Assign reference "text" to the Text composable
      // and constrain it to the bottom of the Button composable
      Text("Text", Modifier.constrainAs(text) {
        top.linkTo(button.bottom, margin = 16.dp)
      })
    }
  }
}
