package com.shapes.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.shapes.app.databinding.FragmentDrawingBinding

class DrawingFragment : Fragment() {
    private var _binding: FragmentDrawingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDrawingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Update UI with current score
        updateScoreDisplay()

        binding.clearButton.setOnClickListener {
            binding.drawingView.clearCanvas()
            Toast.makeText(requireContext(), "Canvas cleared!", Toast.LENGTH_SHORT).show()
        }

        binding.recognizeButton.setOnClickListener {
            recognizeShape()
        }
    }

    private fun recognizeShape() {
        val bitmap = binding.drawingView.cleanBitmap()
        if (bitmap == null) {
            Toast.makeText(requireContext(), "Please draw something first!", Toast.LENGTH_SHORT).show()
            return
        }

        val classifier = MainActivity.shapeClassifier ?: ShapeClassifier()
        val (shape, confidence) = classifier.classify(bitmap)

        // Speak the result
        val message = "This is a $shape!"
        MainActivity.ttsEngine?.speak(message, android.speech.tts.TextToSpeech.QUEUE_FLUSH, null)

        // Navigate to feedback with results
        val action = DrawingFragmentDirections.actionDrawingToFeedback(shape, confidence)
        findNavController().navigate(action)
    }

    private fun updateScoreDisplay() {
        val gameManager = MainActivity.gameManager
        if (gameManager != null) {
            binding.scoreText.text = "Score: ${gameManager.score} | Stars: ${gameManager.stars}"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
