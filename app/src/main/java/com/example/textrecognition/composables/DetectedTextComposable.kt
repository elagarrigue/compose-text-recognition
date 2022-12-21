package com.example.textrecognition.composables

import android.graphics.Rect
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.mlkit.vision.text.Text

@Composable
fun DetectedText(textElement: Text.Line, onTextClicked: (String) -> Unit) {
    Box(
        modifier = Modifier
            .detectedTextSize(textElement.boundingBox)
            .clip(RoundedCornerShape(size = 12.dp))
            .background(Color.Yellow.copy(alpha = 0.5f))
            .clickable {
                onTextClicked(textElement.text)
            }
    ) {
        Text(modifier = Modifier.fillMaxSize(), text = "")
    }
}

private fun Modifier.detectedTextSize(rect: Rect?) =
    this.then(DetectedTextSizeParentData(rect ?: Rect()))

