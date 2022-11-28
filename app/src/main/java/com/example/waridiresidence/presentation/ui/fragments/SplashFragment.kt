package com.example.waridiresidence.presentation.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.waridiresidence.R
import com.example.waridiresidence.databinding.FragmentSplashBinding

class SplashFragment: Fragment(R.layout.fragment_splash) {

    lateinit var binding: FragmentSplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSplashBinding.bind(view)

        Handler().postDelayed({
            //navigate from splash to login
            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
        }, 5000)
    }
}