package es.voghdev.hellocompose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun FormsScreen() {
    Box(
        Modifier
            .fillMaxSize()
    ) {
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            value = TextFieldValue(
                text = "",
                TextRange(1)
            ),
            onValueChange = {}
        )
    }
}