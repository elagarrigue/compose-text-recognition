package com.example.textrecognition.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

data class AdjustedValues(
    val heightDif: Int,
    val widthDif: Int,
    val previewAdjustedWidth: Int,
    val previewAdjustedHeight: Int,
    val scaleW: Float,
    val scaleH: Float,
)

class DetectedObjectPositionState {

    fun getAdjustedValues(
        detectedWidth: Int,
        detectedHeight: Int,
        targetWidth: Int,
        targetHeight: Int
    ): AdjustedValues {


        val heightDif = heightDiff(
            detectedWidth,
            detectedHeight,
            targetWidth,
            targetHeight
        )
        val widthDif = widthDiff(
            detectedWidth,
            detectedHeight,
            targetWidth,
            targetHeight
        )
        val previewAdjustedWidth = targetWidth + widthDif
        val previewAdjustedHeight = targetHeight + heightDif

        return AdjustedValues(
            heightDif = heightDif,
            widthDif = widthDif,
            previewAdjustedWidth = previewAdjustedWidth,
            previewAdjustedHeight = previewAdjustedHeight,
            scaleW = scaleW(detectedWidth, previewAdjustedWidth),
            scaleH = scaleH(detectedHeight, previewAdjustedHeight)
        )
    }

    fun adjustX(x: Int, targetWidth: Int, detectedWidth: Int): Int {
        return if (detectedWidth > 0) targetWidth * x / detectedWidth
        else 0
    }

    fun adjustY(y: Int, targetHeight: Int, detectedHeight: Int): Int {
        return if (detectedHeight > 0) targetHeight * y / detectedHeight
        else 0
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

}

@Composable
fun rememberDetectedObjectPositionState() = remember {
    DetectedObjectPositionState()
}