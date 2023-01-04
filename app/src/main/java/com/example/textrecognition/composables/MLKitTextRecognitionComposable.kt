package com.example.textrecognition.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import com.example.textrecognition.imageanalizer.TextDetectorImageAnalyzer
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

const val PREVIEW_ID = "preview"

@Composable
fun MLKitTextRecognition(onTextClicked: (String) -> Unit) {

    val textRecognizer =
        remember { TextRecognition.getClient(TextRecognizerOptions.Builder().build()) }
    val textDetectorImageAnalyzer = remember {
        TextDetectorImageAnalyzer(textRecognizer)
    }

    val detectedObjects by textDetectorImageAnalyzer.detectedTextBlocksFlow.collectAsState()

    VideoWithMarkers(
        modifier = Modifier.fillMaxSize(),
        detectedObjects.imageProxyWidth,
        detectedObjects.imageProxyHeight,
    ) {

        CameraPreview(
            Modifier.layoutId(PREVIEW_ID),
            textDetectorImageAnalyzer
        )

        detectedObjects.textBlocks.map {
            DetectedText(textElement = it, onTextClicked)
        }
    }
}
