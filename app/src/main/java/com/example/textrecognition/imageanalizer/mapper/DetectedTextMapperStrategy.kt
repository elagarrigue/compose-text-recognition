package com.example.textrecognition.imageanalizer.mapper

import com.example.textrecognition.imageanalizer.DetectedTextBlocks
import com.google.mlkit.vision.text.Text

interface DetectedTextMapperStrategy {

    fun map(textBlocks: List<Text.TextBlock>): List<DetectedTextBlocks.DetectedText>
}

class DetectedBlocksMapper : DetectedTextMapperStrategy {
    override fun map(textBlocks: List<Text.TextBlock>) = textBlocks.map {
        DetectedTextBlocks.DetectedText(it.text, it.boundingBox)
    }
}

class DetectedLinesMapper : DetectedTextMapperStrategy {
    override fun map(textBlocks: List<Text.TextBlock>) = textBlocks.flatMap {
        it.lines.map {
            DetectedTextBlocks.DetectedText(it.text, it.boundingBox)
        }
    }
}

class DetectedElementsMapper : DetectedTextMapperStrategy {
    override fun map(textBlocks: List<Text.TextBlock>) = textBlocks.flatMap {
        it.lines.flatMap {
            it.elements.map {
                DetectedTextBlocks.DetectedText(it.text, it.boundingBox)
            }
        }
    }
}