package com.example.fructiapp

import android.graphics.RectF
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions


class Classifier {
    data class ImageClassifier(val label: String, val score: Float, val index: Int)
    val localModel = LocalModel.Builder()
        .setAssetFilePath("modelClassifier.tflite")
        .build()

    val customImageLabelerOptions = CustomImageLabelerOptions.Builder(localModel)
        .setConfidenceThreshold(0.5f)
        .setMaxResultCount(1)
        .build()
    val imageLabeler = ImageLabeling.getClient(customImageLabelerOptions)

    fun predict(image: InputImage): List<ImageClassifier> {
        var list : List<ImageClassifier> = listOf()
        imageLabeler.process(image)
            .addOnSuccessListener { labels ->
                for (label in labels) {
                    list += (ImageClassifier(label.text, label.confidence, label.index))
                }
            }
            .addOnFailureListener { e ->
                // Task failed with an exception
                // ...
            }
        return list
    }

}