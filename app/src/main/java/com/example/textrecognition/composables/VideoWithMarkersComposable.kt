package com.example.textrecognition.composables

import android.graphics.Rect
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
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


        val heightDif = measurables.first().heightDiff(preview.width, preview.height)
        val widthDif = measurables.first().widthDiff(preview.width, preview.height)

        val scaleW = measurables.first().scaleW(preview.width, widthDif)
        val scaleH = measurables.first().scaleH(preview.height, heightDif)

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

        val detectedText = measurables.takeLast(measurables.size - 2).map {
            val detectedTextSize = it.parentData as DetectedTextSizeParentData
            it.measure(
                constraints.copy(
                    minWidth = (detectedTextSize.rect.width() * scaleW).toInt(),
                    maxWidth = (detectedTextSize.rect.width() * scaleW).toInt(),
                    minHeight = (detectedTextSize.rect.height() * scaleH).toInt(),
                    maxHeight = (detectedTextSize.rect.height() * scaleH).toInt(),
                )
            )
        }

        layout(constraints.maxWidth, constraints.maxHeight) {
            preview.placeRelative(x = 0, y = 0)
            detectedCanvas.placeRelative(x = -widthDif / 2, y = -heightDif / 2)

            detectedText.forEach {
                val detectedTextSize = it.parentData as DetectedTextSizeParentData

                val x = adjustX(
                    detectedTextSize.rect.left,
                    preview.width + widthDif,
                    detectedImageSize.width
                )
                val y = adjustY(
                    detectedTextSize.rect.top,
                    preview.height + heightDif,
                    detectedImageSize.height
                )

                it.placeRelative(x = x - widthDif / 2, y = y - heightDif / 2)
            }
        }
    }
}

private fun Measurable.heightDiff(targetWidth: Int, targetHeight: Int): Int {

    val detectedImageSize = parentData as DetectedImageSizeParentData

    val previewHeight =
        if (detectedImageSize.width > 0)
            targetWidth * detectedImageSize.height / detectedImageSize.width
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

private fun Measurable.scaleH(targetHeight: Int, heightDif: Int): Float {

    val detectedImageSizeParentData = parentData as DetectedImageSizeParentData

    return if (detectedImageSizeParentData.height > 0) (targetHeight + heightDif).toFloat() / detectedImageSizeParentData.height.toFloat()
    else 1f
}

private fun Measurable.scaleW(targetWidth: Int, widthDif: Int): Float {

    val detectedImageSizeParentData = parentData as DetectedImageSizeParentData

    return if (detectedImageSizeParentData.width > 0) (targetWidth + widthDif).toFloat() / detectedImageSizeParentData.width.toFloat()
    else 1f
}

private fun adjustX(x: Int, targetWidth: Int, detectedWidth: Int): Int {

    return if (detectedWidth > 0) targetWidth * x / detectedWidth
    else 0

}

private fun adjustY(y: Int, targetHeight: Int, detectedHeight: Int): Int {

    return if (detectedHeight > 0) targetHeight * y / detectedHeight
    else 0

}