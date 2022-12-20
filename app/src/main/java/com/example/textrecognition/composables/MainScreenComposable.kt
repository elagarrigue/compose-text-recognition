package com.example.textrecognition.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.textrecognition.imageanalizer.ObjectDetectorImageAnalyzer
import com.google.mlkit.vision.text.TextRecognition


@Composable
fun MLKitTextRecognition() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TextRecognitionView()
    }
}

@Composable
fun TextRecognitionView() {

    val textRecognizer = remember { TextRecognition.getClient() }
    val objectDetectorImageAnalyzer = remember {
        ObjectDetectorImageAnalyzer(textRecognizer)
    }

    val detectedObjects by objectDetectorImageAnalyzer.boxFlow.collectAsState()

    VideoWithMarkers(
        modifier = Modifier
            .fillMaxSize()
    ) {

        CameraPreview(
            detectedObjects.imageProxyWidth,
            detectedObjects.imageProxyHeight,
            objectDetectorImageAnalyzer
        )

        detectedObjects.textBlocks.map { 
            DetectedText(textBlock = it)
        }
    }
}
