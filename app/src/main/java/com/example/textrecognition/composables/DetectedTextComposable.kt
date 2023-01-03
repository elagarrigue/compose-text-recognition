package com.example.textrecognition.composables

import android.graphics.Rect
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.textrecognition.imageanalizer.DetectedTextBlocks

@Composable
fun DetectedText(textElement: DetectedTextBlocks.DetectedText, onTextClicked: (String) -> Unit) {
    Canvas(
        modifier = Modifier
            .detectedTextSize(textElement.rect)
            .clip(RoundedCornerShape(size = 12.dp))
            .clickable {
                onTextClicked(textElement.text)
            }
    ) {
        drawRect(Color.Yellow.copy(alpha = 0.5f))
    }
}

private fun Modifier.detectedTextSize(rect: Rect?) =
    this.then(DetectedTextSizeParentData(rect ?: Rect()))

