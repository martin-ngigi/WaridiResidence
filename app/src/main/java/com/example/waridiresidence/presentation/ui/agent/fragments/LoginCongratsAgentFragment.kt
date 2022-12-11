package com.example.waridiresidence.presentation.ui.agent.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.waridiresidence.R
import com.example.waridiresidence.databinding.FragmentLoginCongratsAgentBinding

class LoginCongratsAgentFragment : Fragment(R.layout.fragment_login_congrats_agent) {

    lateinit var  binding: FragmentLoginCongratsAgentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLoginCongratsAgentBinding.bind(view)
    }
}