package com.example.waridiresidence.presentation.ui.agent.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.waridiresidence.R
import com.example.waridiresidence.databinding.FragmentAddHouseAgentBinding

class AddHouseAgentFragment: Fragment(R.layout.fragment_add_house_agent){

    lateinit var binding: FragmentAddHouseAgentBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddHouseAgentBinding.bind(view)
    }
}