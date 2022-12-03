package com.example.waridiresidence.presentation.ui.client.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.waridiresidence.R
import com.example.waridiresidence.databinding.FragmentSettingsClientBinding


class SettingsClientFragment : Fragment(R.layout.fragment_settings_client) {
    private lateinit var binding: FragmentSettingsClientBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * init binding
         */
        binding = FragmentSettingsClientBinding.bind(view)
    }
}