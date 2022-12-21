package com.example.textrecognition.composables

class DetectedObjectPositionState(
    private val detectedWidth: Int,
    private val detectedHeight: Int,
    targetWidth: Int,
    targetHeight: Int
) {

    private val previewAdjustedWidth: Int
    private val previewAdjustedHeight: Int
    val heightDif: Int
    val widthDif: Int
    val scaleW: Float
    val scaleH: Float

    init {
        heightDif = heightDiff(
            detectedWidth,
            detectedHeight,
            targetWidth,
            targetHeight
        )
        widthDif = widthDiff(
            detectedWidth,
            detectedHeight,
            targetWidth,
            targetHeight
        )
        previewAdjustedWidth = targetWidth + widthDif
        previewAdjustedHeight = targetHeight + heightDif

        scaleW = scaleW(detectedWidth, previewAdjustedWidth)
        scaleH = scaleH(detectedHeight, previewAdjustedHeight)

    }

    fun adjustX(x: Int): Int {
        return if (detectedWidth > 0) previewAdjustedWidth * x / detectedWidth
        else 0
    }

    fun adjustY(y: Int): Int {
        return if (detectedHeight > 0) previewAdjustedHeight * y / detectedHeight
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
