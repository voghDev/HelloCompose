package es.voghdev.hellocompose

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FormsScreen() {
    Box(
        Modifier
            .fillMaxSize()
    ) {
        val colors = TextFieldDefaults.outlinedTextFieldColors()
        val interactionSource = remember { MutableInteractionSource() }

        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .indicatorLine(
                    enabled = true,
                    isError = false,
                    colors = colors,
                    interactionSource = interactionSource,
                    focusedIndicatorLineThickness = 1.dp,
                    unfocusedIndicatorLineThickness = 1.dp
                )
                .padding(bottom = 80.dp),
            value = TextFieldValue(
                text = "Text 1",
                TextRange(1)
            ),
            onValueChange = {}
        ) {
            TextFieldDefaults.OutlinedTextFieldDecorationBox(
                value = "",
                visualTransformation = VisualTransformation.None,
                innerTextField = it,
                // same interaction source as the one passed to BasicTextField to read focus state
                // for text field styling
                interactionSource = interactionSource,
                enabled = true,
                singleLine = true,
                // update border thickness and shape
                border = {
                    TextFieldDefaults.BorderBox(
                        enabled = true,
                        isError = false,
                        colors = colors,
                        interactionSource = interactionSource,
                        shape = CircleShape,
                        unfocusedBorderThickness = 1.dp,
                        focusedBorderThickness = 1.dp
                    )
                }
            )
        }

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            value = "Text 2",
            onValueChange = {},
            colors = colors
        )
    }
}