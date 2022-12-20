package com.example.textrecognition.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.unit.Density

class DetectedImageSizeParentData(val width: Int, val height: Int) : ParentDataModifier {

    override fun Density.modifyParentData(parentData: Any?) = this@DetectedImageSizeParentData
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

        val heightDif = measurables.first().heightDiff(preview.width, preview.height)
        val widthDif = measurables.first().widthDiff(preview.width, preview.height)

        val detectedCanvas = measurables[1].run {
            measure(
                constraints.copy(
                    minWidth = preview.width + widthDif,
                    maxWidth = preview.width + widthDif,
                    minHeight = preview.height + heightDif,
                    maxHeight = preview.height + heightDif,
                )

            )
        }

        layout(constraints.maxWidth, constraints.maxHeight) {
            preview.placeRelative(x = 0, y = 0)
            detectedCanvas.placeRelative(x = -widthDif / 2, y = -heightDif / 2)

        }
    }
}

private fun Measurable.heightDiff(targetWidth: Int, targetHeight: Int): Int {

    val detectedImageSizeParentData = parentData as DetectedImageSizeParentData

    val previewHeight =
        if (detectedImageSizeParentData.width > 0)
            targetWidth * detectedImageSizeParentData.height / detectedImageSizeParentData.width
        else targetHeight

    val heightDiff = previewHeight - targetHeight

    return if (heightDiff > 0) heightDiff else 0
}

private fun Measurable.widthDiff(targetWidth: Int, targetHeight: Int): Int {

    val detectedImageSizeParentData = parentData as DetectedImageSizeParentData

    val previewWidth =
        if (detectedImageSizeParentData.height > 0)
            targetHeight * detectedImageSizeParentData.width / detectedImageSizeParentData.height
        else targetWidth

    val heightDiff = previewWidth - targetWidth

    return if (heightDiff > 0) heightDiff else 0
}