package com.example.textrecognition.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.textrecognition.imageanalizer.TextDetectorImageAnalyzer
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions


@Composable
fun MLKitTextRecognition(onTextClicked: (String) -> Unit) {

    val textRecognizer =
        remember { TextRecognition.getClient(TextRecognizerOptions.Builder().build()) }
    val textDetectorImageAnalyzer = remember {
        TextDetectorImageAnalyzer(textRecognizer)
    }

    val detectedObjects by textDetectorImageAnalyzer.detectedTextBlocksFlow.collectAsState()

    VideoWithMarkers(
        modifier = Modifier.fillMaxSize()
    ) {

        CameraPreview(
            detectedObjects.imageProxyWidth,
            detectedObjects.imageProxyHeight,
            textDetectorImageAnalyzer
        )

        detectedObjects.textBlocks.map {
            DetectedText(textElement = it, onTextClicked)
        }
    }
}
