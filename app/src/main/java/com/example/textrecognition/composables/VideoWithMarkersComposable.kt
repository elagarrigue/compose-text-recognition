package com.example.textrecognition.composables

import android.graphics.Rect
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density

class DetectedTextRectParentData(val rect: Rect) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?) = this@DetectedTextRectParentData
}

@Composable
fun VideoWithMarkers(
    modifier: Modifier = Modifier,
    detectedImageWidth: Int, detectedImageHeight: Int,
    content: @Composable () -> Unit,
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->

        val preview = measurePreview(measurables, constraints)

        val detectedStateHolder =
            getDetectedTextPositionStateHolder(preview, detectedImageWidth, detectedImageHeight)

        val detectedText =
            measureDetectedTextElements(measurables, constraints, detectedStateHolder)

        layout(constraints.maxWidth, constraints.maxHeight) {
            placePreview(preview)
            placeDetectedText(detectedText, detectedStateHolder)
        }
    }
}

private fun measurePreview(
    measurables: List<Measurable>,
    constraints: Constraints
) = measurables.first { it.layoutId == PREVIEW_ID }.measure(constraints)

private fun getDetectedTextPositionStateHolder(
    preview: Placeable,
    detectedImageWidth: Int,
    detectedImageHeight: Int
): DetectedTextPositionState {
    return DetectedTextPositionState(
        detectedImageWidth, detectedImageHeight, preview.width, preview.height
    )
}

private fun measureDetectedTextElements(
    measurables: List<Measurable>,
    constraints: Constraints,
    detectedState: DetectedTextPositionState
) = measurables.filter { it.parentData is DetectedTextRectParentData }.map {
    val detectedTextRect = (it.parentData as DetectedTextRectParentData).rect
    it.measure(
        constraints.copy(
            minWidth = (detectedTextRect.width() * detectedState.scaleW).toInt(),
            maxWidth = (detectedTextRect.width() * detectedState.scaleW).toInt(),
            minHeight = (detectedTextRect.height() * detectedState.scaleH).toInt(),
            maxHeight = (detectedTextRect.height() * detectedState.scaleH).toInt(),
        )
    )
}

private fun Placeable.PlacementScope.placePreview(preview: Placeable) {
    preview.placeRelative(x = 0, y = 0)
}

private fun Placeable.PlacementScope.placeDetectedText(
    detectedText: List<Placeable>,
    detectedState: DetectedTextPositionState
) {
    detectedText.forEach {
        val detectedTextRect = (it.parentData as DetectedTextRectParentData).rect
        val x = detectedState.adjustX(detectedTextRect.left)
        val y = detectedState.adjustY(detectedTextRect.top)

        it.placeRelative(
            x = x - detectedState.widthDif / 2,
            y = y - detectedState.heightDif / 2
        )
    }
}
