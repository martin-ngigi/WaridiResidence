package com.example.waridiresidence.presentation.ui.agent.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.waridiresidence.R
import com.example.waridiresidence.databinding.FragmentHomeAgentBinding

class HomeAgentFragment:  Fragment(R.layout.fragment_home_agent) {

    lateinit var binding: FragmentHomeAgentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeAgentBinding.bind(view)
    }
}