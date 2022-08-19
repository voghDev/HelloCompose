package es.voghdev.hellocompose

import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
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
                text = "Custom text field 1",
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
                    UnderlineBox(
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

@ExperimentalMaterialApi
@Composable
private fun UnderlineBox(
    enabled: Boolean,
    isError: Boolean,
    interactionSource: InteractionSource,
    colors: TextFieldColors,
    shape: Shape = TextFieldDefaults.OutlinedTextFieldShape,
    focusedBorderThickness: Dp = TextFieldDefaults.FocusedBorderThickness,
    unfocusedBorderThickness: Dp = TextFieldDefaults.UnfocusedBorderThickness
) {
    Layout(modifier = Modifier, content = { }) { measurables, constraints ->
        val rows = 1
        // Keep track of the width of each row
        val rowWidths = IntArray(rows) { 0 }

        // Keep track of the max height of each row
        val rowHeights = IntArray(rows) { 0 }
        val placeables = measurables.mapIndexed { index, measurable ->

            // Measure each child
            val placeable = measurable.measure(constraints)
            placeable
        }
        val width = rowWidths.maxOrNull()
            ?.coerceIn(constraints.minWidth.rangeTo(constraints.maxWidth))
            ?: constraints.minWidth

        // Grid's height is the sum of the tallest element of each row
        // coerced to the height constraints
        val height = rowHeights.sumOf { it }
            .coerceIn(constraints.minHeight.rangeTo(constraints.maxHeight))

        val rowY = IntArray(rows) { 0 }
        for (i in 1 until rows) {
            rowY[i] = rowY[i - 1] + rowHeights[i - 1]
        }
        layout(width, height) {
            val rowX = IntArray(rows) { 0 }
            placeables.forEachIndexed { index, placeable ->
                val row = index % rows
                placeable.placeRelative(
                    x = rowX[row],
                    y = rowY[row]
                )
                rowX[row] += placeable.width
            }
        }
    }
}