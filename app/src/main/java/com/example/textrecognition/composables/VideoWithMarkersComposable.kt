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
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->

        val preview = measurables.first().measure(constraints)
        val detectedImageSize = measurables.first().parentData as DetectedImageSizeParentData
        val detectedState = DetectedObjectPositionState(
            detectedImageSize.width, detectedImageSize.height, preview.width, preview.height
        )

        val detectedText = measurables.takeLast(measurables.size - 1).map {
            val detectedTextRect = (it.parentData as DetectedTextSizeParentData).rect
            it.measure(
                constraints.copy(
                    minWidth = (detectedTextRect.width() * detectedState.scaleW).toInt(),
                    maxWidth = (detectedTextRect.width() * detectedState.scaleW).toInt(),
                    minHeight = (detectedTextRect.height() * detectedState.scaleH).toInt(),
                    maxHeight = (detectedTextRect.height() * detectedState.scaleH).toInt(),
                )
            )
        }

        layout(constraints.maxWidth, constraints.maxHeight) {
            preview.placeRelative(x = 0, y = 0)

            detectedText.forEach {
                val detectedTextRect = (it.parentData as DetectedTextSizeParentData).rect
                val x = detectedState.adjustX(detectedTextRect.left)
                val y = detectedState.adjustY(detectedTextRect.top)

                it.placeRelative(
                    x = x - detectedState.widthDif / 2,
                    y = y - detectedState.heightDif / 2
                )
            }
        }
    }
}



