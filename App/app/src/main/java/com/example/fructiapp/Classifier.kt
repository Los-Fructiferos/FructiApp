package com.example.fructiapp

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.Image
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.custom.CustomObjectDetectorOptions
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
/**
 * Analyzes an image using ML Kit.
 */
class MLKitObjectDetector(val bitIm :Bitmap, val rot: Int)  {
    data class DetectedObjectResult(
        val confidence: Float,
        val label: String,
        val centerCoordinate: Pair<Int, Int>
    )
    // To use a custom model, follow steps on https://developers.google.com/ml-kit/vision/object-detection/custom-models/android.
    val model = LocalModel.Builder().setAssetFilePath("model.tflite").build()
    val builder = CustomObjectDetectorOptions.Builder(model)

    // For the ML Kit default model, use the following:
    //val builder = ObjectDetectorOptions.Builder()

    private val options = builder
        .setDetectorMode(CustomObjectDetectorOptions.SINGLE_IMAGE_MODE)
        .enableClassification()
        .enableMultipleObjects()
        .build()
    private val detector = ObjectDetection.getClient(options)

    suspend fun analyze(image: Image, imageRotation: Int): List<DetectedObjectResult> {
        // `image` is in YUV (https://developers.google.com/ar/reference/java/com/google/ar/core/Frame#acquireCameraImage()),

        // The model performs best on upright images, so rotate it.
        val rotatedImage = Bitmap.createBitmap(
            bitIm, 0, 0, bitIm.width, bitIm.height, Matrix().apply {
                postRotate(rot.toFloat())}, true)

        val inputImage = InputImage.fromBitmap(rotatedImage, 0)

        val mlKitDetectedObjects = detector.process(inputImage).asDeferred().await()
        return mlKitDetectedObjects.mapNotNull { obj ->
            val bestLabel = obj.labels.maxByOrNull { label -> label.confidence } ?: return@mapNotNull null
            val coords = obj.boundingBox.exactCenterX().toInt() to obj.boundingBox.exactCenterY().toInt()
            val rotatedCoordinates = coords.rotateCoordinates(rotatedImage.width, rotatedImage.height, imageRotation)
            DetectedObjectResult(bestLabel.confidence, bestLabel.text, rotatedCoordinates)
        }
    }

    @Suppress("USELESS_IS_CHECK")
    fun hasCustomModel() = builder is CustomObjectDetectorOptions.Builder
}