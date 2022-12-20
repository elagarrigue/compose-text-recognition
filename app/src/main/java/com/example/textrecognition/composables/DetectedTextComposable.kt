package com.example.textrecognition.composables

import android.graphics.Rect
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import com.example.textrecognition.imageanalizer.DetectedTextBlocks
import com.google.mlkit.vision.text.Text

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


@Composable
fun DetectedText2(textBlock: Text.TextBlock) {
    Box(
        modifier = Modifier
            .background(Color.Red.copy(alpha = 0.5f))
            .detectedTextSize(textBlock.boundingBox)
            .clickable {
                Log.e("EMM", "Clicked ${textBlock.text}")
            }
    ) {

        Text(modifier = Modifier.fillMaxSize(), text = textBlock.text)
    }

}

private fun Modifier.detectedTextSize(rect: Rect?) =
    this.then(DetectedTextSizeParentData(rect ?: Rect()))

