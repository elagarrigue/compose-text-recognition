package com.example.textrecognition.composables

import android.graphics.Rect
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import com.example.textrecognition.DetectedTextBlocks

@Composable
fun DetectedText(detectedTextBlocksObjects: DetectedTextBlocks) {

    Canvas(modifier = Modifier) {

        detectedTextBlocksObjects.textBlocks.map {
            it.boundingBox.adjustedPosition(
                detectedTextBlocksObjects.imageProxyWidth,
                detectedTextBlocksObjects.imageProxyHeight,
                this.size.width.toInt(),
                this.size.height.toInt(),
            )
        }.forEach {

            drawRect(
                color = Color.Gray.copy(alpha = 0.5f),
                topLeft = Offset(it.left.toFloat(), it.top.toFloat()),
                size = Size(it.width().toFloat(), it.height().toFloat())
            )
        }
    }
}

private fun Rect?.adjustedPosition(w1: Int, h1: Int, w2: Int, h2: Int): Rect {

    return this?.let {
        Rect(
            left.correct(w1, w2),
            top.correct(h1, h2),
            right.correct(w1, w2),
            bottom.correct(h1, h2),
        )
    }
        ?: Rect()
}

private fun Int.correct(n: Int, m: Int) = this * m / n