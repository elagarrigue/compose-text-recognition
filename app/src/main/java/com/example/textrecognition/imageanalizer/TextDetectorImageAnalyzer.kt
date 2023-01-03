package com.example.textrecognition.imageanalizer

import android.annotation.SuppressLint
import android.graphics.Rect
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognizer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update


data class DetectedTextBlocks(
    val imageProxyWidth: Int = 0,
    val imageProxyHeight: Int = 0,
    val textBlocks: List<DetectedText> = listOf()
) {
    data class DetectedText(val text: String, val rect: Rect?)
}

class TextDetectorImageAnalyzer(
    private val textRecognizer: TextRecognizer
) : ImageAnalysis.Analyzer {


    val boxFlow = MutableStateFlow(DetectedTextBlocks())

    private val debounceTime = 200L
    private var timeStamp = 0L

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            val rotationDegrees = imageProxy.imageInfo.rotationDegrees

            textRecognizer.process(image)
                .addOnCompleteListener {
                    imageProxy.close()
                }
                .addOnSuccessListener { text ->
                    if (System.currentTimeMillis() - timeStamp > debounceTime) {
                        timeStamp = System.currentTimeMillis()

                        val w = if (rotationDegrees == 0) imageProxy.width else imageProxy.height
                        val h = if (rotationDegrees == 0) imageProxy.height else imageProxy.width

                        boxFlow.update {
                            DetectedTextBlocks(
                                w,
                                h,
                                text.textBlocks.toDetectedLines()
                            )
                        }
                    }
                }
        }
    }
}

private fun List<Text.TextBlock>.toDetectedLines(): List<DetectedTextBlocks.DetectedText> =
    flatMap {
        it.lines.map {
            DetectedTextBlocks.DetectedText(it.text, it.boundingBox)
        }
    }
