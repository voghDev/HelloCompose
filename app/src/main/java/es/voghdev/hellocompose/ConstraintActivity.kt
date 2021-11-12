package es.voghdev.hellocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import es.voghdev.hellocompose.ui.theme.HelloComposeTheme

class ConstraintActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      HelloComposeTheme {
        LargeConstraintLayout()
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

  @Composable
  fun LargeConstraintLayout() {
    ConstraintLayout {
      val text = createRef()

      val guideline = createGuidelineFromStart(fraction = 0.5f)
      Text(
        "This is a very very very very very very very long text",
        Modifier.constrainAs(text) {
          linkTo(start = guideline, end = parent.end)
          width = Dimension.preferredWrapContent
        }
      )
    }
  }

  @Preview
  @Composable
  fun LargeConstraintLayoutPreview() {
    HelloComposeTheme {
      LargeConstraintLayout()
    }
  }
}
