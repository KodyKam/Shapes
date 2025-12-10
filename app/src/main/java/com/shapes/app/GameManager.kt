package com.shapes.app

class GameManager {
    var score = 0
    var stars = 0
    var correctAnswers = 0
    var totalAttempts = 0

    fun addScore(points: Int) {
        score += points
        if (score >= 100) {
            stars++
            score = 0
        }
    }

    fun recordAttempt(correct: Boolean) {
        totalAttempts++
        if (correct) {
            correctAnswers++
            addScore(10)
        }
    }

    fun getAccuracy(): Int {
        return if (totalAttempts > 0) (correctAnswers * 100) / totalAttempts else 0
    }

    fun reset() {
        score = 0
        stars = 0
        correctAnswers = 0
        totalAttempts = 0
    }
}
