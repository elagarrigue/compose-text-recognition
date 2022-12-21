package com.example.textrecognition.composables

import android.graphics.Rect
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.mlkit.vision.text.Text

@Composable
fun DetectedText(textBlock: Text.TextBlock, onTextClicked: (String) -> Unit) {
    Box(
        modifier = Modifier
            .background(Color.Red.copy(alpha = 0.5f))
            .detectedTextSize(textBlock.boundingBox)
            .clickable {
                onTextClicked(textBlock.text)
            }
    ) {

        Text(modifier = Modifier.fillMaxSize(), text = textBlock.text)
    }

}

private fun Modifier.detectedTextSize(rect: Rect?) =
    this.then(DetectedTextSizeParentData(rect ?: Rect()))

