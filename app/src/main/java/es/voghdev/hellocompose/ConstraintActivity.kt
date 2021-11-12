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

class ConstraintActivity : ComponentActivity() {
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
      // Creates references for the three composables
      // in the ConstraintLayout's body
      val (button1, button2, text) = createRefs()

      Button(
        onClick = { /* Do something */ },
        modifier = Modifier.constrainAs(button1) {
          top.linkTo(parent.top, margin = 16.dp)
        }
      ) {
        Text("Button 1")
      }

      Text("Text", Modifier.constrainAs(text) {
        top.linkTo(button1.bottom, margin = 16.dp)
        centerAround(button1.end)
      })

      val barrier = createEndBarrier(button1, text)
      Button(
        onClick = { /* Do something */ },
        modifier = Modifier.constrainAs(button2) {
          top.linkTo(parent.top, margin = 16.dp)
          start.linkTo(barrier)
        }
      ) {
        Text("Button 2")
      }
    }
  }
}
