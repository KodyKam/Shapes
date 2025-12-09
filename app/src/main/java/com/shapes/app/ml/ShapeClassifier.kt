package com.shapes.app.ml

import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ShapeClassifier(context: Context) {

    private val interpreter: Interpreter

    init {
        val modelBytes = context.assets.open("model.tflite").readBytes()

        val buffer = ByteBuffer.allocateDirect(modelBytes.size)
        buffer.order(ByteOrder.nativeOrder())
        buffer.put(modelBytes)
        buffer.rewind()

        interpreter = Interpreter(buffer)
    }

    fun classify(bitmap: Bitmap): String {
        // temporary placeholder
        return "Circle"
    }
}