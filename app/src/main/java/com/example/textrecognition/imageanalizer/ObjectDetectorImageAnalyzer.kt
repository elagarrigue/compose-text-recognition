package com.example.textrecognition

import android.annotation.SuppressLint
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
    val textBlocks: List<Text.TextBlock> = listOf()
)

class ObjectDetectorImageAnalyzer(
    private val textRecognizer: TextRecognizer,
) : ImageAnalysis.Analyzer {


    val boxFlow = MutableStateFlow(DetectedTextBlocks())

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
                    val w = if (rotationDegrees == 0) imageProxy.width else imageProxy.height
                    val h = if (rotationDegrees == 0) imageProxy.height else imageProxy.width

                    boxFlow.update {
                        DetectedTextBlocks(w, h, text.textBlocks)
                    }
                }
        }
    }
}
