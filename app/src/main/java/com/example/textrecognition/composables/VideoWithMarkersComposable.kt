package com.example.textrecognition.composables

import android.graphics.Rect
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.unit.Density

class DetectedImageSizeParentData(val width: Int, val height: Int) : ParentDataModifier {

    override fun Density.modifyParentData(parentData: Any?) = this@DetectedImageSizeParentData
}

class DetectedTextSizeParentData(val rect: Rect) : ParentDataModifier {

    override fun Density.modifyParentData(parentData: Any?) = this@DetectedTextSizeParentData
}

@Composable
fun VideoWithMarkers(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {

    val detectedObjectPositionState = rememberDetectedObjectPositionState()

    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->

        val preview = measurables.first().measure(constraints)

        val detectedImageSize = measurables.first().parentData as DetectedImageSizeParentData

        val adjustedValues = detectedObjectPositionState.getAdjustedValues(
            detectedImageSize.width, detectedImageSize.height, preview.width, preview.height
        )


        val detectedText = measurables.takeLast(measurables.size - 1).map {
            val detectedTextSize = it.parentData as DetectedTextSizeParentData
            it.measure(
                constraints.copy(
                    minWidth = (detectedTextSize.rect.width() * adjustedValues.scaleW).toInt(),
                    maxWidth = (detectedTextSize.rect.width() * adjustedValues.scaleW).toInt(),
                    minHeight = (detectedTextSize.rect.height() * adjustedValues.scaleH).toInt(),
                    maxHeight = (detectedTextSize.rect.height() * adjustedValues.scaleH).toInt(),
                )
            )
        }

        layout(constraints.maxWidth, constraints.maxHeight) {
            preview.placeRelative(x = 0, y = 0)

            detectedText.forEach {
                val detectedTextSize = it.parentData as DetectedTextSizeParentData

                val x = detectedObjectPositionState.adjustX(
                    detectedTextSize.rect.left,
                    adjustedValues.previewAdjustedWidth,
                    detectedImageSize.width
                )
                val y = detectedObjectPositionState.adjustY(
                    detectedTextSize.rect.top,
                    adjustedValues.previewAdjustedHeight,
                    detectedImageSize.height
                )

                it.placeRelative(
                    x = x - adjustedValues.widthDif / 2,
                    y = y - adjustedValues.heightDif / 2
                )
            }
        }
    }
}



