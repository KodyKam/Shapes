/*
 * File: ShapeRecognizer.kt
 * Author: Dingpei Chen
 * Purpose: Coordinates recognition flow between drawing input and ML classifier
 * Last Modified: Dec 2025
 */

package com.shapes.app

import android.graphics.Path
import kotlin.math.sqrt

class ShapeRecognizer {

    fun recognizeShape(path: Path): Pair<String, Int> {
        // For now, return circle with 92% confidence
        // In a real app, you would use ML Kit or TensorFlow
        return Pair("circle", 92)
    }

    fun calculateShapeFeatures(points: List<Pair<Float, Float>>): Map<String, Float> {
        if (points.size < 3) return emptyMap()

        val features = mutableMapOf<String, Float>()

        // Calculate bounding box
        val minX = points.minOf { it.first }
        val maxX = points.maxOf { it.first }
        val minY = points.minOf { it.second }
        val maxY = points.maxOf { it.second }

        val width = maxX - minX
        val height = maxY - minY

        features["width"] = width
        features["height"] = height
        features["aspectRatio"] = if (height > 0) width / height else 1f

        // Calculate circularity
        val perimeter = calculatePerimeter(points)
        val area = calculateArea(points)
        val circularity = if (perimeter > 0) (4 * Math.PI * area) / (perimeter * perimeter) else 0f
        features["circularity"] = circularity.toFloat()

        return features
    }

    private fun calculatePerimeter(points: List<Pair<Float, Float>>): Float {
        var perimeter = 0f
        for (i in 0 until points.size - 1) {
            val dx = points[i + 1].first - points[i].first
            val dy = points[i + 1].second - points[i].second
            perimeter += sqrt((dx * dx + dy * dy).toDouble()).toFloat()
        }
        return perimeter
    }

    private fun calculateArea(points: List<Pair<Float, Float>>): Float {
        var area = 0f
        for (i in 0 until points.size - 1) {
            area += points[i].first * points[i + 1].second
            area -= points[i + 1].first * points[i].second
        }
        return (area / 2).coerceAtLeast(0f)
    }
}
