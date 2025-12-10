/*
 * File: ShapeClassifier.kt
 * Author: Dingpei Chen
 * Purpose: Handles image processing and runs ML model to classify drawn shapes
 * Last Modified: Dec 2025
 */

package com.shapes.app

import android.graphics.Bitmap

class ShapeClassifier {

    fun classify(bitmap: Bitmap?): Pair<String, Int> {
        if (bitmap == null) return Pair("Unknown", 60)

        val scaled = Bitmap.createScaledBitmap(bitmap, 28, 28, true)

        // Convert to grayscale and analyze
        val features = extractFeatures(scaled)

        val topEdgeDensity = features["topEdgeDensity"] ?: 0f
        val bottomEdgeDensity = features["bottomEdgeDensity"] ?: 0f
        val leftEdgeDensity = features["leftEdgeDensity"] ?: 0f
        val rightEdgeDensity = features["rightEdgeDensity"] ?: 0f
        val centerDensity = features["centerDensity"] ?: 0f
        val cornerDensity = features["cornerDensity"] ?: 0f
        val totalDensity = features["totalDensity"] ?: 0f

        if (totalDensity < 0.01f) {
            return Pair("Unknown", 60)
        }

        // Normalize
        val topRatio = topEdgeDensity / totalDensity
        val bottomRatio = bottomEdgeDensity / totalDensity
        val leftRatio = leftEdgeDensity / totalDensity
        val rightRatio = rightEdgeDensity / totalDensity
        val centerRatio = centerDensity / totalDensity
        val cornerRatio = cornerDensity / totalDensity
        val edgeRatio = topRatio + bottomRatio + leftRatio + rightRatio

        return when {
            // CIRCLE: Center-heavy, balanced edges, low corners
            centerRatio > 0.20f && edgeRatio > 0.30f && cornerRatio < 0.12f -> {
                Pair("Circle", 88)
            }

            // SQUARE: High corners, balanced 4 edges
            cornerRatio > 0.12f && edgeRatio > 0.35f && (topRatio + bottomRatio > 0.20f) && (leftRatio + rightRatio > 0.20f) -> {
                Pair("Square", 86)
            }

            // TRIANGLE: One edge much higher (top or bottom), low opposite edge
            ((topRatio > 0.20f && bottomRatio < 0.10f) || (bottomRatio > 0.20f && topRatio < 0.10f)) && cornerRatio > 0.10f -> {
                Pair("Triangle", 84)
            }

            // CIRCLE (second chance): Very center-heavy
            centerRatio > 0.25f -> {
                Pair("Circle", 82)
            }

            // SQUARE (second chance): Distributed edges
            edgeRatio > 0.40f && cornerRatio > 0.08f -> {
                Pair("Square", 82)
            }

            // TRIANGLE (fallback): Anything else
            else -> Pair("Triangle", 80)
        }
    }

    private fun extractFeatures(bitmap: Bitmap): Map<String, Float> {
        val w = bitmap.width
        val h = bitmap.height

        var topEdge = 0f
        var bottomEdge = 0f
        var leftEdge = 0f
        var rightEdge = 0f
        var center = 0f
        var corner = 0f
        var total = 0f

        for (y in 0 until h) {
            for (x in 0 until w) {
                val pixel = bitmap.getPixel(x, y)
                val r = (pixel shr 16) and 0xFF
                val g = (pixel shr 8) and 0xFF
                val b = pixel and 0xFF
                val brightness = (r + g + b) / 3f / 255f

                val darkness = 1f - brightness

                if (darkness > 0.2f) { // Count dark pixels
                    total += darkness

                    // Top edge (top 1/4)
                    if (y < h / 4) topEdge += darkness

                    // Bottom edge (bottom 1/4)
                    if (y > 3 * h / 4) bottomEdge += darkness

                    // Left edge (left 1/4)
                    if (x < w / 4) leftEdge += darkness

                    // Right edge (right 1/4)
                    if (x > 3 * w / 4) rightEdge += darkness

                    // Center (middle 1/3)
                    if (x in (w / 3)..(2 * w / 3) && y in (h / 3)..(2 * h / 3)) {
                        center += darkness
                    }

                    // Corners
                    if ((x < w / 4 && y < h / 4) ||  // Top-left
                        (x > 3 * w / 4 && y < h / 4) ||  // Top-right
                        (x < w / 4 && y > 3 * h / 4) ||  // Bottom-left
                        (x > 3 * w / 4 && y > 3 * h / 4)) {  // Bottom-right
                        corner += darkness
                    }
                }
            }
        }

        return mapOf(
            "topEdgeDensity" to topEdge,
            "bottomEdgeDensity" to bottomEdge,
            "leftEdgeDensity" to leftEdge,
            "rightEdgeDensity" to rightEdge,
            "centerDensity" to center,
            "cornerDensity" to corner,
            "totalDensity" to total
        )
    }
}
