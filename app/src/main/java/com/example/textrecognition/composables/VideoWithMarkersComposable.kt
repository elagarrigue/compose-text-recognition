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

        val heightDif = heightDiff(
            detectedImageSize.width,
            detectedImageSize.height,
            preview.width,
            preview.height
        )
        val widthDif = widthDiff(
            detectedImageSize.width,
            detectedImageSize.height,
            preview.width,
            preview.height
        )

        val previewAdjustedWidth = preview.width + widthDif
        val previewAdjustedHeight = preview.height + heightDif

        val scaleW = scaleW(detectedImageSize.width, previewAdjustedWidth)
        val scaleH = scaleH(detectedImageSize.height, previewAdjustedHeight)

        val detectedText = measurables.takeLast(measurables.size - 1).map {
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

            detectedText.forEach {
                val detectedTextSize = it.parentData as DetectedTextSizeParentData

                val x = adjustX(
                    detectedTextSize.rect.left,
                    previewAdjustedWidth,
                    detectedImageSize.width
                )
                val y = adjustY(
                    detectedTextSize.rect.top,
                    previewAdjustedHeight,
                    detectedImageSize.height
                )

                it.placeRelative(x = x - widthDif / 2, y = y - heightDif / 2)
            }
        }
    }
}

private fun heightDiff(
    detectedWidth: Int,
    detectedHeight: Int,
    targetWidth: Int,
    targetHeight: Int
): Int {

    val previewHeight =
        if (detectedWidth > 0)
            targetWidth * detectedHeight / detectedWidth
        else targetHeight

    val heightDiff = previewHeight - targetHeight

    return if (heightDiff > 0) heightDiff else 0
}

private fun widthDiff(
    detectedWidth: Int,
    detectedHeight: Int,
    targetWidth: Int, targetHeight: Int
): Int {

    val previewWidth =
        if (detectedHeight > 0)
            targetHeight * detectedWidth / detectedHeight
        else targetWidth

    val heightDiff = previewWidth - targetWidth

    return if (heightDiff > 0) heightDiff else 0
}

private fun scaleH(detectedHeight: Int, targetHeight: Int): Float {
    return if (detectedHeight > 0) targetHeight.toFloat() / detectedHeight.toFloat()
    else 1f
}

private fun scaleW(detectedWidth: Int, targetWidth: Int): Float {
    return if (detectedWidth > 0) targetWidth.toFloat() / detectedWidth.toFloat()
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