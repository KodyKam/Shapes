/*
 * File: HomeFragment.kt
 * Author: Kunj Kharadi
 * Purpose: Displays the home screen and allows users to start the Shapes game
 * Last Modified: Dec 2025
 */

package com.shapes.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.shapes.app.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Add animations
        val fadeIn = AnimationUtils.loadAnimation(requireContext(), android.R.anim.fade_in)
        binding.logoImage.startAnimation(fadeIn)
        binding.appTitle.startAnimation(fadeIn)
        binding.appSubtitle.startAnimation(fadeIn)

        binding.startGameButton.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_drawing)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
