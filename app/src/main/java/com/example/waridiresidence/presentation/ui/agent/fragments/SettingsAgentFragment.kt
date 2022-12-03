package com.example.waridiresidence.presentation.ui.agent.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.waridiresidence.R
import com.example.waridiresidence.databinding.FragmentSettingsAgentBinding


class SettingsAgentFragment : Fragment(R.layout.fragment_settings_agent) {
    private lateinit var binding: FragmentSettingsAgentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * init binding
         */
        binding = FragmentSettingsAgentBinding.bind(view)
    }
}