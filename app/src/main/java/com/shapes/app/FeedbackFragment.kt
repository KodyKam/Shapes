/*
 * File: FeedbackFragment.kt
 * Author: Kunj Kharadi
 * Purpose: Displays feedback based on recognized shape and user performance
 * Last Modified: Dec 2025
 */

package com.shapes.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.shapes.app.databinding.FragmentFeedbackBinding

class FeedbackFragment : Fragment() {
    private var _binding: FragmentFeedbackBinding? = null
    private val binding get() = _binding!!
    private val args: FeedbackFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedbackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val shape = args.shape
        val confidence = args.confidence

        // Display feedback
        binding.confidenceText.text = "You drew: $shape ($confidence%)"

        // Update game score
        val gameManager = MainActivity.gameManager
        if (gameManager != null) {
            gameManager.recordAttempt(confidence >= 80)
            binding.scoreText.text = "${gameManager.score}"
            binding.accuracyText.text = "${gameManager.getAccuracy()}%"
        }

        // Game 1: Shape Matching
        binding.playGame1Btn.setOnClickListener {
            playGame1(shape)
        }

        // Game 2: Speed Quiz
        binding.playGame2Btn.setOnClickListener {
            playGame2()
        }

        // Game 3: Shape Counter
        binding.playGame3Btn.setOnClickListener {
            playGame3()
        }

        // Mini-Game: Show 3 shape options
        displayMiniGame(shape)

        binding.tryAgainButton.setOnClickListener {
            findNavController().navigate(R.id.action_feedback_to_drawing)
        }

        binding.nextButton.setOnClickListener {
            findNavController().navigate(R.id.action_feedback_to_drawing)
        }
    }

    private fun displayMiniGame(correctShape: String) {
        val shapes = listOf("Circle", "Square", "Triangle")
        val options = shapes.shuffled().take(3)

        binding.option1.text = "⭕ ${options.getOrNull(0) ?: "Circle"}"
        binding.option2.text = "🔷 ${options.getOrNull(1) ?: "Square"}"
        binding.option3.text = "🔺 ${options.getOrNull(2) ?: "Triangle"}"

        binding.option1.setOnClickListener {
            checkAnswer(options.getOrNull(0) ?: "Circle", correctShape)
        }
        binding.option2.setOnClickListener {
            checkAnswer(options.getOrNull(1) ?: "Square", correctShape)
        }
        binding.option3.setOnClickListener {
            checkAnswer(options.getOrNull(2) ?: "Triangle", correctShape)
        }
    }

    private fun checkAnswer(selected: String, correct: String) {
        val gameManager = MainActivity.gameManager
        val message = if (selected == correct) {
            gameManager?.addScore(20)
            "🎉 Correct! You got it!"
        } else {
            "❌ Try again! It was $correct"
        }

        binding.miniGameResult.text = message
        binding.miniGameResult.visibility = View.VISIBLE
    }

    private fun playGame1(shape: String) {
        binding.game1Container.visibility = if (binding.game1Container.visibility == View.VISIBLE) View.GONE else View.VISIBLE
    }

    private fun playGame2() {
        Toast.makeText(requireContext(), "⚡ Speed Quiz: Identify shapes in 5 seconds! Coming soon!", Toast.LENGTH_LONG).show()
    }

    private fun playGame3() {
        Toast.makeText(requireContext(), "🔢 Shape Counter: Count all the shapes! Coming soon!", Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
